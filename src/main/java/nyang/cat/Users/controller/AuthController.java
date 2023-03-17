package nyang.cat.Users.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import nyang.cat.Users.dto.UsersRequestDto;
import nyang.cat.Users.dto.UsersResponseDto;
import nyang.cat.Users.service.KakaoUsersService;
import nyang.cat.jwt.JwtDto;
import nyang.cat.jwt.JwtRequestDto;
import nyang.cat.Users.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final KakaoUsersService kakaoUsersService;


    @PostMapping("/signup")
    public ResponseEntity<UsersResponseDto> signup(@RequestBody UsersRequestDto usersRequestDto) {
        return ResponseEntity.ok(authService.signup(usersRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody UsersRequestDto usersRequestDto) {

        return ResponseEntity.ok(authService.login(usersRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtDto> reissue(@RequestBody JwtRequestDto jwtRequestDto) {
        return ResponseEntity.ok(authService.reissue(jwtRequestDto));
    }


        /* 카카오 로그인 */
    @GetMapping("/kakao/callback")
    public ResponseEntity<JwtDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        /* 카카오 서버로부터 받은 인가 코드 */
        System.out.println("code = " + code);

        return ResponseEntity.ok(kakaoUsersService.kakaoLogin(code));
    }
}