package nyang.cat.board.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class ExController {
    @GetMapping("/hello")
    public List<String> Hello() {
        return Arrays.asList("냠냠냠냠냠");
    }
}
