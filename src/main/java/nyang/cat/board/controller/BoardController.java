package nyang.cat.board.controller;

import lombok.RequiredArgsConstructor;
import nyang.cat.board.dto.SearchHandler;
import nyang.cat.board.entity.Board;
import nyang.cat.board.repository.BoardRepository;
import nyang.cat.board.service.BoardService;
import nyang.cat.multipart.FileUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    /*   모든 게시글 목록 (페이징)   */
    @GetMapping("/boards")
    public ResponseEntity<Map<String, Object>> readBoardList(@PageableDefault(size = 4, sort = "pno",
            direction = Sort.Direction.DESC) Pageable pageable,
                                                             @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                                             @RequestParam(required = false, defaultValue = "main", value = "category") String category,
                                                             @RequestParam(required = false,  value="option") String option,
                                                             @RequestParam(required = false,  value="keyword") String keyword, SearchHandler sc) {

        System.out.println(" pageNo "+pageNo);
        System.out.println(" option "+option);
        System.out.println("keyword = " + keyword);
        System.out.println("sc = " + sc);
        System.out.println("category = " + category);

        Map<String, Object> map = boardService.getBoardList(pageable, sc, pageNo, category);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/board")
    public Object read(@RequestParam("pno") Long pno) {
        try {
            Board post = boardService.findPost(pno);
            return post;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("READ ERROR", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/feeds")
    public ResponseEntity<Map<String, Object>> showFeed(@PageableDefault(size = 10, sort = "pno",
            direction = Sort.Direction.DESC) Pageable pageable,
                                                             @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                                             @RequestParam(required = false, defaultValue = "main", value = "category") String category,
                                                             @RequestParam(required = false,  value="option") String option,
                                                             @RequestParam(required = false,  value="keyword") String keyword, SearchHandler sc) {

        Map<String, Object> map = boardService.getBoardList(pageable, sc, pageNo, category);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    /*     게시물 작성      */
    @PostMapping("/board")
                        /* RequestBody 로는 이미지파일을 못받아서 수정*/
    public Object write(Authentication authentication,
                        @RequestParam("title") String title,
                        @RequestParam("content") String content,
                        @RequestParam("category") String category,
                        @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        if(authentication != null) {
            Board board = new Board();
            board.setTitle(title);
            board.setContent(content);
            board.setCategory(category);
            System.out.println("category? = " + category);

            if(file != null) {

                /* 저장할 파일 이름 */
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                /* 저장할 경로 */
                String filePath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/" + fileName;
                /* 파일 저장 */
                file.transferTo(new File(filePath));
                board.setImageName(fileName);
            }
            return boardService.save(authentication, board);

        }
        return new ResponseEntity<>("AUTH_ERR", HttpStatus.BAD_REQUEST);
    }

    /*    게시물 삭제    */
    @DeleteMapping("/board")
    public Object delete(@RequestParam Long pno, Authentication authentication) {
        if (authentication != null) {
            try {
                boolean delete = boardService.delete(authentication, pno);

                if (delete)
                    return new ResponseEntity<>("DELETE_OK", HttpStatus.OK);

            } catch (Exception e) {
                return new ResponseEntity<>("DELETE_ERR", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("AUTH_ERR", HttpStatus.BAD_REQUEST);
    }

    /*    게시물 수정    */
    @PatchMapping("/board")
    public Object modify(@RequestParam Long pno, @RequestBody Board board) {
       board.setPno(pno);
        return boardService.modify(board);
    }


}
