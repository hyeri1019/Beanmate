package nyang.cat.board.service;

import lombok.RequiredArgsConstructor;
import nyang.cat.board.dto.Pagination;
import nyang.cat.board.dto.SearchHandler;
import nyang.cat.board.entity.Board;
import nyang.cat.board.repository.BoardRepository;
import nyang.cat.repository.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UsersRepository usersRepository;


    /* ------------------ 페이징 ------------------*/
    public Map getBoardList(Pageable pageable, SearchHandler sc, int pageNo) {
        Map map = new HashMap();

        sc = Optional.ofNullable(sc).orElse(new SearchHandler());

        String option = sc.getOption();
        String keyword = sc.getKeyword();

        Page<Board> boardList = null;
        List<Board> listContent = null;

        if (option.equals("제목")) {
            boardList = boardRepository.findByTitleContaining(keyword, pageable);
            listContent = boardList.getContent();
           }

        else if(option.equals("제목 내용")) {
            System.out.println(" 제목+내용으로 검색 ");
            boardList = boardRepository.findByTitleContainingOrContentContaining(keyword,keyword,pageable);
            listContent = boardList.getContent();
        }

        else if(option.equals("작성자")) {
            System.out.println(" 작성자로 검색 ");
            boardList = boardRepository.findByWriterContaining(keyword,pageable);
            listContent = boardList.getContent();
        }


        else {
            boardList = boardRepository.findAll(pageable);
            listContent = boardList.getContent();

        }
        map.put("posts", listContent);


        int totalPage = boardList.getTotalPages();
        /*  시작페이지 1 / 11 / 21 ...   */
        int startPage = (int) ((Math.floor(pageNo / 11) * 10) + 1
                <= totalPage ? (Math.floor(pageNo / 11) * 10) + 1 : totalPage);

        /*  한 nav 당 페이지 개수 : 10 / 20 / 30...   */
        int endPage = (startPage + 10 - 1 < totalPage ? startPage + 10 - 1 : totalPage);
        System.out.println("endPage = " + endPage);

        boolean hasPrev = boardList.hasPrevious();
        boolean hasNext = boardList.hasNext();

        int prevIndex = boardList.previousOrFirstPageable().getPageNumber() + 1;
        int nextIndex = boardList.nextOrLastPageable().getPageNumber() + 1;

        map.put("pagination", new Pagination(totalPage, startPage, endPage, hasPrev, hasNext, prevIndex, nextIndex));
        System.out.println("페이지네이션="+map.get("pagination"));

        return map;
    }

    /* ------------------ 글읽기 ------------------*/
    public Board findPost(Long pno) {
        Board board = boardRepository.findById(pno).orElseThrow(() -> new RuntimeException(String.valueOf(pno)));

        /* 클릭하면 조회수 증가 */
        int viewCnt = board.getViewCnt();
        board.setViewCnt(viewCnt + 1);
        boardRepository.save(board);

        return board;
    }

    /* ------------------ 글쓰기 ------------------*/
    public Board save(String username, Board board) {
        /* 작성자 set */
        board.setWriter(username);
        return boardRepository.save(board);
    }

    /* ------------------ 글삭제 ------------------*/
    public void deletePost(Long pno) {
        boardRepository.deleteById(pno);

    }

    /* ------------------ 글수정 ------------------*/
    public Board modifyPost(Board board) {
        System.out.println("board = " + board);
        return boardRepository.save(board);
    }

}