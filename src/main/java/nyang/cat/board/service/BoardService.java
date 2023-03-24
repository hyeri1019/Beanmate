package nyang.cat.board.service;

import lombok.RequiredArgsConstructor;
import nyang.cat.Users.service.UsersService;
import nyang.cat.board.dto.Pagination;
import nyang.cat.board.dto.SearchHandler;
import nyang.cat.board.entity.Board;
import nyang.cat.board.repository.BoardRepository;
import nyang.cat.Users.entity.Users;
import nyang.cat.Users.repository.UsersRepository;
import nyang.cat.patron.entity.PatronTier;
import nyang.cat.patron.repository.PatronTierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UsersRepository usersRepository;
    private final UsersService usersService;
    private final PatronTierRepository patronTierRepository;


    /* ------------------ 페이징 + 검색 ------------------*/
    public Map<String, Object> getBoardList(Pageable pageable, SearchHandler sc, int pageNo, String category) {
        System.out.println("sc = " + sc);
        System.out.println("category = " + category);

        Map<String, Object> map = new HashMap<>();

        sc = Optional.ofNullable(sc).orElse(new SearchHandler());

        String option = sc.getOption();
        String keyword = sc.getKeyword();

        Page<Board> boardList = null;
        List<Board> listContent = null;

        if(category.equals("feeds")){
            boardList = boardRepository.findAll(pageable);
            listContent = boardList.getContent();
        }
        else if (option.equals("제목")) {
            boardList = boardRepository.findByTitleContaining(keyword, pageable);
            listContent = boardList.getContent();
        } else if (option.equals("제목 내용")) {
            System.out.println(" 제목+내용으로 검색 ");
            boardList = boardRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
            listContent = boardList.getContent();
        } else if (option.equals("작성자")) {
            System.out.println(" 작성자로 검색 ");
            boardList = boardRepository.findByWriterContaining(keyword, pageable);
            listContent = boardList.getContent();
        } else {
            boardList = boardRepository.findByCategory(category, pageable);
            listContent = boardList.getContent();

        }
        map.put("posts", listContent);


        int totalPage = boardList.getTotalPages();
        int startPage = (int) ((Math.floor((pageNo - 1) / 10) * 10) + 1
                <= totalPage ? (Math.floor((pageNo - 1) / 10) * 10) + 1 : totalPage);

        int endPage = (startPage + 10 - 1 < totalPage ? startPage + 10 - 1 : totalPage);
        System.out.println("endPage = " + endPage);

        boolean hasPrev = boardList.hasPrevious();
        boolean hasNext = boardList.hasNext();

        int prevIndex = boardList.previousOrFirstPageable().getPageNumber() + 1;
        int nextIndex = boardList.nextOrLastPageable().getPageNumber() + 1;

        map.put("pagination", new Pagination(totalPage, startPage, endPage, hasPrev, hasNext, prevIndex, nextIndex));
        System.out.println("페이지네이션=" + map.get("pagination"));

        return map;
    }

    public Map<String, Object> boardListByCreator(String creator, Pageable pageable, int pageNo) {

        Map<String, Object> map = new HashMap<>();

        Page<Board> boardList = null;
        List<Board> listContent = null;

        boardList = boardRepository.findByWriter(creator, pageable);
        listContent = boardList.getContent();

        map.put("posts", listContent);

        int totalPage = boardList.getTotalPages();
        int startPage = (int) ((Math.floor((pageNo - 1) / 10) * 10) + 1
                <= totalPage ? (Math.floor((pageNo - 1) / 10) * 10) + 1 : totalPage);

        int endPage = (startPage + 10 - 1 < totalPage ? startPage + 10 - 1 : totalPage);

        boolean hasPrev = boardList.hasPrevious();
        boolean hasNext = boardList.hasNext();

        int prevIndex = boardList.previousOrFirstPageable().getPageNumber() + 1;
        int nextIndex = boardList.nextOrLastPageable().getPageNumber() + 1;

        map.put("pagination", new Pagination(totalPage, startPage, endPage, hasPrev, hasNext, prevIndex, nextIndex));

        return map;
    }


    /* ------------------ 글읽기 ------------------*/
    public Object findPost(Long pno, Authentication authentication) {
        Users requestUser = usersService.getUserInfo(authentication);

        Board board = boardRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException(String.valueOf(pno)));

        System.out.println("================"+board.getWriter());
        System.out.println("================"+requestUser.getName());

        if(board.getAuthLevel().equals("public") || board.getWriter().equals(requestUser.getName())) {
            return board;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("authorities ==== " + authorities);

        String requiredAuthority = "ROLE_"+board.getAuthLevel();
        System.out.println("requiredAuthority = " + requiredAuthority);
        Boolean hasAccess = authorities.stream().anyMatch(authority -> authority.getAuthority().equals(requiredAuthority));

       return hasAccess;

    }

    /* ------------------ 글쓰기 ------------------*/
    @Transactional
    public Board save(Authentication authentication, String title, String content, String category, String authLevel, MultipartFile file) throws IOException {
        Users user = usersService.getUserInfo(authentication);
        String fileName = null;

        if(file != null) {
            /* 저장할 파일 이름 */
            fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            /* 저장할 경로 */
            String filePath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/" + fileName;
            /* 파일 저장 */
            file.transferTo(new File(filePath));
        }

        if(!authLevel.equals("public")) {
        authLevel = user.getName()+"_"+authLevel;
        }

        Board board = Board.builder()
                .title(title)
                .user(user)
                .writer(user.getName())
                .content(content)
                .category(category)
                .authLevel(authLevel)
                .imageName(fileName)
                .build();

        System.out.println("board = " + board);

        return boardRepository.save(board);

    }

    /* ------------------ 글삭제 ------------------*/
    public boolean delete(Authentication authentication, Long pno) {
        Users user = usersService.getUserInfo(authentication);
        String requestUser = user.getEmail();
        String writer = null;

        Optional<Board> findPost = boardRepository.findById(pno);
        if (findPost.isPresent()) {
            Board board = findPost.get();
            writer = board.getUser().getEmail();
        }
        if (requestUser.equals(writer)) {
            boardRepository.deleteById(pno);
            return true;
        }
        return false;
    }

    /* ------------------ 글수정 ------------------*/
    public boolean modify(Board board, Authentication authentication) {
        Users user = usersService.getUserInfo(authentication);

        String requestUser = user.getEmail();
        String writer = board.getUser().getEmail();

        if (requestUser.equals(writer)) {
            boardRepository.save(board);
            return true;
        }
        return false;
    }

}