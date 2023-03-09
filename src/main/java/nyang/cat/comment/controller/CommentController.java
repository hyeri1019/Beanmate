package nyang.cat.comment.controller;

import lombok.RequiredArgsConstructor;
import nyang.cat.comment.service.CommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public Object commentList(@PageableDefault(size = 10, sort = "pno", direction = Sort.Direction.DESC) Pageable pageable,
                              @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                              @RequestParam(required = false, value = "pno") Long pno) {

        Map<String, Object> map = commentService.getCommentList(pageable, pageNo, pno);
        return map;
    }
}
