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

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CreatorController {

    private final CreatorService creatorService;

    @GetMapping("/creator")
    public ResponseEntity<Map<String, Object>> showCreatorPage(@PageableDefault(size = 10, sort = "pno",
                                 direction = Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                 @RequestParam(required = true, value = "creator") String creator, SearchHandler sc) {

        System.out.println("creator = " + creator);

        Map<String, Object> map = creatorService.showCreatorPage(creator, pageable, sc, pageNo);

        return new ResponseEntity<>(map, HttpStatus.OK);

    }

    @PostMapping("/creator")
    public void creatorRegister(
            @RequestBody Creator profile, Authentication authentication) {

        creatorService.creatorRegister(profile, authentication);
    }
}
