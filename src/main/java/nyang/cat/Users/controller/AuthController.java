package nyang.cat.Users.controller;

import lombok.RequiredArgsConstructor;
import nyang.cat.Users.dto.UsersRequestDto;
import nyang.cat.Users.dto.UsersResponseDto;
import nyang.cat.jwt.JwtDto;
import nyang.cat.jwt.JwtRequestDto;
import nyang.cat.Users.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

   private final AuthService authService;

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
}
