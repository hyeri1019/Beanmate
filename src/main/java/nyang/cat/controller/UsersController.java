package nyang.cat.controller;


import lombok.RequiredArgsConstructor;
import nyang.cat.dto.JwtDto;
import nyang.cat.dto.JwtRequestDto;
import nyang.cat.dto.UsersResponseDto;
import nyang.cat.jwt.SecurityUtil;
import nyang.cat.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/me")
    public ResponseEntity<Authentication> findMemberInfoById(@RequestBody JwtRequestDto jwtRequestDto) {
        System.out.println("jwtRequestDto = " + jwtRequestDto);
        return ResponseEntity.ok(usersService.myPage(jwtRequestDto));
    }
}

