package nyang.cat.patron.service;

import lombok.RequiredArgsConstructor;
import nyang.cat.Users.entity.Users;
import nyang.cat.Users.service.UsersService;
import nyang.cat.board.dto.Pagination;
import nyang.cat.board.dto.SearchHandler;
import nyang.cat.board.entity.Board;
import nyang.cat.board.repository.BoardRepository;
import nyang.cat.board.service.BoardService;
import nyang.cat.patron.entity.Creator;
import nyang.cat.patron.repository.CreatorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CreatorService {

    private final CreatorRepository creatorRepository;
    private final BoardRepository boardRepository;
    private final UsersService usersService;


    public Map<String, Object> creatorPageAndCreatorInfo(String creator, Pageable pageable, SearchHandler sc, int pageNo) {
        Map map = new HashMap();

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

        Creator creatorInfo = creatorRepository.findByName(creator).orElse(null);
        map.put("creatorInfo", creatorInfo);

        return map;
    }



    public void creatorRegister(String about, MultipartFile profileBackground, Authentication authentication) throws IOException {
        Users user = usersService.getUserInfo(authentication);
        String fileName = null;

        if(profileBackground != null) {
            /* 저장할 파일 이름 */
            fileName = UUID.randomUUID() + "_" + profileBackground.getOriginalFilename();
            /* 저장할 경로 */
            String filePath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/creator/" + fileName;
            /* 파일 저장 */
            profileBackground.transferTo(new File(filePath));
        }

        Creator creator = Creator.builder()
                .user(user)
                .about(about)
                .name(user.getName())
                .profileBackground(fileName)
                .build();

        creatorRepository.save(creator);
    }

}
