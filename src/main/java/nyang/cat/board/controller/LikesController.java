package nyang.cat.board.controller;

import lombok.RequiredArgsConstructor;
import nyang.cat.board.service.LikesService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @GetMapping("/likes")
    public Boolean isLiked(@RequestParam("pno") Long pno, Authentication authentication) {

        return likesService.isLiked(pno, authentication);
    }

    /* 좋아요 중복 방지 */
    @PostMapping("/likes")
    public Boolean increase(@RequestParam("pno") Long pno, Authentication authentication) {

        return likesService.increase(pno, authentication);
    }


}