package nyang.cat.Users.controller;


import lombok.RequiredArgsConstructor;
import nyang.cat.Users.dto.UsersRequestDto;
import nyang.cat.Users.entity.Users;
import nyang.cat.Users.service.UsersService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/me")
    public Object findMemberInfoById(Authentication authentication) {

        Users users = usersService.myPage(authentication);
        return users;
    }

    @PatchMapping("/me")
    public void update(Authentication authentication, @RequestBody Users user) {
        usersService.update(authentication, user);
    }
}

