package nyang.cat.board.controller;

import lombok.RequiredArgsConstructor;
import nyang.cat.board.dto.SearchHandler;
import nyang.cat.board.entity.Board;
import nyang.cat.board.repository.BoardRepository;
import nyang.cat.board.service.BoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /*   모든 게시글 목록 (페이징)   */
    @GetMapping("/boards")
    public ResponseEntity<Map<String, Object>> readBoardList(@PageableDefault(size = 10, sort = "pno", direction = Sort.Direction.DESC) Pageable pageable,
                                                             @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                                             @RequestParam(required = false, defaultValue = "main", value = "category") String category,
                                                             @RequestParam(required = false,  value="option") String option,
                                                             @RequestParam(required = false,  value="keyword") String keyword, SearchHandler sc) {

        Map<String, Object> map = boardService.getBoardList(pageable, sc, pageNo, category);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/board")
    public Object read(@RequestParam("pno") Long pno, Authentication authentication) {

        Board post = boardService.findPost(pno);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("authorities ==== " + authorities);

        String requiredAuthority = "ROLE_"+post.getAuthLevel();
        System.out.println("requiredAuthority = " + requiredAuthority);
        boolean hasAccess = authorities.stream().anyMatch(authority -> authority.getAuthority().equals(requiredAuthority));

        if(hasAccess) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("FIND_NOT_AUTH");
        }
    }

    @GetMapping("/feeds")
    public ResponseEntity<Map<String, Object>> showFeed(@PageableDefault(size = 10, sort = "pno",
                                                            direction = Sort.Direction.DESC) Pageable pageable,
                                                             @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                                             @RequestParam(required = false, defaultValue = "feeds", value = "category") String category,
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
                        @RequestParam("authLevel") String authLevel,
                        @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        if(authentication != null) {
            return boardService.save(authentication, title, content, category, authLevel, file);
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
    public Object modify(@RequestBody Board board, Authentication authentication) {
        return boardService.modify(board, authentication);
    }
}
