package nyang.cat.patron.service;

import lombok.RequiredArgsConstructor;
import nyang.cat.Users.entity.Users;
import nyang.cat.Users.repository.UsersRepository;
import nyang.cat.Users.service.CustomUserDetailsService;
import nyang.cat.Users.service.UsersService;
import nyang.cat.board.dto.Pagination;
import nyang.cat.board.dto.SearchHandler;
import nyang.cat.board.entity.Board;
import nyang.cat.board.repository.BoardRepository;
import nyang.cat.board.service.BoardService;
import nyang.cat.jwt.Authority;
import nyang.cat.patron.entity.Creator;
import nyang.cat.patron.repository.CreatorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CreatorService {

    private final CreatorRepository creatorRepository;
    private final BoardRepository boardRepository;
    private final UsersService usersService;
    private final UsersRepository usersRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final SubscriptionService subscriptionService;


    public Creator creatorInfo(String creator) {

        return creatorRepository.findByName(creator).orElse(null);
    }



    @Transactional
    public void creatorRegister(String about, MultipartFile profileImage, MultipartFile profileBackground, Authentication authentication) throws IOException {
        Users user = usersService.getUserInfo(authentication);
        String profileFileName = null;
        String backgroundFileName = null;

        if (profileImage != null && profileBackground != null) {

            profileFileName = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            backgroundFileName = UUID.randomUUID() + "_" + profileBackground.getOriginalFilename();

            String profileFilePath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/creator/" + profileFileName;
            profileImage.transferTo(new File(profileFilePath));

            String bgFilePath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/creator/" + backgroundFileName;
            profileBackground.transferTo(new File(bgFilePath));
        }

        Creator creator = Creator.builder()
                .user(user)
                .about(about)
                .name(user.getName())
                .profileBackground(backgroundFileName)
                .profileImage(profileFileName)
                .build();

        user.setAuthority(Authority.ROLE_CREATOR);
        usersRepository.save(user);
        creatorRepository.save(creator);
    }

}
