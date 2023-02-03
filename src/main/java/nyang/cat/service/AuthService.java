package nyang.cat.service;

import lombok.RequiredArgsConstructor;
import nyang.cat.dto.JwtDto;
import nyang.cat.dto.JwtRequestDto;
import nyang.cat.dto.UsersRequestDto;
import nyang.cat.dto.UsersResponseDto;
import nyang.cat.entity.SecurityUsers;
import nyang.cat.entity.Users;
import nyang.cat.jwt.JwtTokenProvider;
import nyang.cat.jwt.RefreshToken;
import nyang.cat.jwt.RefreshTokenRepository;
import nyang.cat.repository.UsersRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public UsersResponseDto signup(UsersRequestDto usersRequestDto) {
        if(usersRepository.existsByEmail(usersRequestDto.getEmail())) {
            throw new RuntimeException("이미 가입된 id 입니다.");
        }
        Users user = usersRequestDto.toMember(passwordEncoder);
        return UsersResponseDto.of(usersRepository.save(user));
    }

    @Transactional
    /* access token */
    public JwtDto login(UsersRequestDto usersRequestDto) {
        System.out.println(" 로그인 서비스 ");
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = usersRequestDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtDto jwtDto = tokenProvider.generateToken(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                                    .key(authentication.getName())
                                    .value(jwtDto.getRefreshToken())
                                    .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return jwtDto;
    }

    @Transactional
    /* refresh token */
    public JwtDto reissue(JwtRequestDto jwtRequestDto) {
        System.out.println("refreshToken 확인");
        // 1. Refresh Token 검증
        if (tokenProvider.tokenValidation(jwtRequestDto.getRefreshToken())>0) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(jwtRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
        
        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(jwtRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        JwtDto tokenDto = tokenProvider.generateToken(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);
        
        

        // 토큰 발급
        return tokenDto;
    }
}
