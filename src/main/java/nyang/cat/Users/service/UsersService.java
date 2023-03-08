package nyang.cat.Users.service;

import lombok.RequiredArgsConstructor;
import nyang.cat.Users.dto.UsersRequestDto;
import nyang.cat.Users.entity.Users;
import nyang.cat.Users.repository.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public Users getUserInfo(Authentication authentication) {
        Long username = Long.valueOf(authentication.getName());

        Optional<Users> findUser = usersRepository.findById(username);
        if (findUser.isPresent()) {
            Users user = findUser.get();
            return user;
        }
        throw new RuntimeException("회원정보를 찾을 수 없습니다.");
    }

    public Users myPage(Authentication authentication){
        Users user = getUserInfo(authentication);
        return user;
    }

    public void update(Authentication authentication, Users user) {
        Users userInfo = getUserInfo(authentication);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        userInfo.setPassword(encodedPassword);
        usersRepository.save(userInfo);

    }
}



