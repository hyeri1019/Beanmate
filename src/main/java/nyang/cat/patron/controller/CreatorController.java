package nyang.cat.patron.controller;

import lombok.RequiredArgsConstructor;
import nyang.cat.board.dto.SearchHandler;
import nyang.cat.board.entity.Board;
import nyang.cat.patron.entity.Creator;
import nyang.cat.patron.repository.CreatorRepository;
import nyang.cat.patron.service.CreatorService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CreatorController {

    private final CreatorService creatorService;

    @GetMapping("/creator")
    public ResponseEntity<Map<String, Object>> showCreatorPageAndCreatorInfo(@PageableDefault(size = 10, sort = "pno",
                                 direction = Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                 @RequestParam(required = true, value = "creator") String creator, SearchHandler sc) {

        Map<String, Object> map = creatorService.creatorPageAndCreatorInfo(creator, pageable, sc, pageNo);
        return new ResponseEntity<>(map, HttpStatus.OK);

    }

    @PostMapping("/creator")
    public void creatorRegister(
            @RequestParam(value = "about") String about,
            @RequestParam(value = "profileBackground", required = false) MultipartFile profileBackground, Authentication authentication) throws IOException {

            creatorService.creatorRegister(about, profileBackground, authentication);
    }
}
