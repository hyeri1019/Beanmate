package nyang.cat.service;

import lombok.RequiredArgsConstructor;
import nyang.cat.dto.JwtRequestDto;
import nyang.cat.dto.UsersResponseDto;
import nyang.cat.jwt.JwtTokenProvider;
import nyang.cat.repository.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersService {

    private static  JwtTokenProvider tokenProvider;

//    public UsersResponseDto findMemberInfoById(Long memberId) {
//        System.out.println("memberId = " + memberId);
//        return usersRepository.findById(memberId)
//                .map(UsersResponseDto::of)
//                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
//    }
//
//    public UsersResponseDto findMemberInfoByEmail(String email) {
//        return usersRepository.findByEmail(email)
//                .map(UsersResponseDto::of)
//                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
//    }
    public Authentication myPage(JwtRequestDto jwtRequestDto){
        var accessToken = jwtRequestDto.getAccessToken();
        return tokenProvider.getAuthentication(accessToken);
    }

}



