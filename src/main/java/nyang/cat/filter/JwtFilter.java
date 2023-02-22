package nyang.cat.filter;

import lombok.RequiredArgsConstructor;
import nyang.cat.dto.JwtDto;
import nyang.cat.dto.JwtRequestDto;
import nyang.cat.jwt.JwtTokenProvider;
import nyang.cat.jwt.RefreshToken;
import nyang.cat.jwt.RefreshTokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    /* SecurityContext  */

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // JWT 토큰의 인증 정보를 SecurityContext 에 저장
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException, IOException {

        // 1. Request Header 에서 토큰을 꺼냄
        String token = resolveToken(request);
        System.out.println("Request Header 에서 가져온 access token = " + token);

        // 2. validateToken 으로 토큰 유효성 검사
        // 정상 토큰이면 Authentication 을  SecurityContext 에 저장 (해당 사용자 인증 처리)
        if (StringUtils.hasText(token) && tokenProvider.tokenValidation(token)==0) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println(" 유효한 토큰입니다. ");
        }
        /* 다음 필터 호출 */
        filterChain.doFilter(request, response);

    }

    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        // "Authorization" 헤더 값이 존재하고 "Bearer "로 시작하는 경우
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            System.out.println("Request Header 에서 토큰 꺼내기 = " + bearerToken);
            // "Bearer "를 제거하고 JWT 토큰을 반환
            return bearerToken.substring(7);
        }
        return null;
    }

}