package nyang.cat.Users.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import nyang.cat.Users.entity.Users;
import nyang.cat.Users.repository.UsersRepository;
import nyang.cat.jwt.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoUsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Transactional
    /*  인가 코드로 엑세스 토큰을 요청 */
    public JwtDto kakaoLogin(String code)  throws JsonProcessingException {
        String accessToken = getAccessToken(code, "http://localhost:3000/auth/kakao/callback");

        Users kakaoUser = registerKakaoUserIfNeeded(accessToken);

        JwtDto jwtDto = jwtTokenCreate(kakaoUser.getEmail());

        return jwtDto;
    }

    private String getAccessToken(String code, String redirect_uri) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirect_uri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
                );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    /*  회원이 아닌 경우 회원가입 진행  */
    private Users registerKakaoUserIfNeeded(String accessToken) throws JsonProcessingException {
        JsonNode jsonNode = getKakaoUserInfo(accessToken);

        /*    중복된 Email이 없으면 회원가입 수행   */
        String kakaoEmail = jsonNode.get("kakao_account").get("email").asText();
        Users kakaoUser = usersRepository.findByEmail(kakaoEmail).orElse(null);

        if (kakaoUser == null) {
            String kakaoNick = jsonNode.get("properties").get("nickname").asText();

            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            kakaoUser = Users.builder()
                    .email(kakaoEmail)
                    .name(kakaoNick)
                    .authority(Authority.ROLE_USER)
                    .password(encodedPassword)
                    .build();

            usersRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

    private JsonNode getKakaoUserInfo(String accessToken) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        /* HTTP 요청 보내기 */
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoUserInfoRequest,
                    String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(responseBody);
    }

    /* JWT 토큰 생성 */
    private JwtDto jwtTokenCreate(String username) {

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtDto jwtDto = tokenProvider.generateToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(jwtDto.getRefreshToken())
                .build();
                refreshTokenRepository.save(refreshToken);

                return jwtDto;
    }
}
