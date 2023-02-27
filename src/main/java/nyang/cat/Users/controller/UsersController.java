package nyang.cat.Users.controller;


import lombok.RequiredArgsConstructor;
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
        System.out.println("마이페이지  " + authentication);

        Users users = usersService.myPage(authentication);
        System.out.println("users = " + users);
        return users;
    }
}

