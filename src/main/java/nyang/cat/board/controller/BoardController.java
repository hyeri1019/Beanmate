package nyang.cat.board.controller;

import lombok.RequiredArgsConstructor;
import nyang.cat.board.dto.Pagination;
import nyang.cat.board.dto.SearchHandler;
import nyang.cat.board.entity.Board;
import nyang.cat.board.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /*   모든 게시글 목록 (페이징)   */
    @GetMapping("/boards")
    public Object readBoardList(@PageableDefault(size = 10, sort = "pno",
            direction = Sort.Direction.DESC) Pageable pageable,
                                @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                @RequestParam(required = false,  value="option") String option,
                                @RequestParam(required = false,  value="keyword") String keyword, SearchHandler sc) {

        System.out.println(" pageNo "+pageNo);
        System.out.println(" option "+option);
        System.out.println("keyword = " + keyword);
        System.out.println("sc = " + sc);

        return boardService.getBoardList(pageable, sc, pageNo);
    }

    /*      게시물 읽기      */
    @GetMapping("/board")
    public Object read(@RequestParam("pno") Long pno) {

        return boardService.findPost(pno);
    }

    /*     게시물 작성      */
    @PostMapping("/board")
    public Object write(@RequestBody Board board) {

        System.out.println("board write = " + board);
        String username = "asdf";
        System.out.println(" board post ");
        return boardService.save(username, board);
    }

//    @PostMapping("/board")
//    public String write(@Valid Board board, Model model, BindingResult bindingResult, RedirectAttributes redirect,
//            /* 토큰 인증이 된 주체를 가져옴 */
//                        Authentication authentication) {
//
//        String username = authentication.getName();
//        try {
//            boardService.save(username, board);
//            model.addAttribute(board);
//            redirect.addFlashAttribute("message", "write ok");
//            return "redirect:/board";
//        } catch (Exception e) {
//            model.addAttribute("message", "write failed");
//            return "boardList";
//        }
//    }

    /*    게시물 삭제    */
    @DeleteMapping("/board")
    public void delete(@RequestParam Long pno) {
        System.out.println("pno = " + pno);
        boardService.deletePost(pno);

    }

    /*    게시물 수정    */
    @PatchMapping("/board")
    public Object modify(@RequestParam Long pno, @RequestBody Board board) {
       board.setPno(pno);
        return boardService.modifyPost(board);
    }


}
