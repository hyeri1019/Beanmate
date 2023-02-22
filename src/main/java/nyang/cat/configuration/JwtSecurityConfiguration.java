package nyang.cat.configuration;

import lombok.RequiredArgsConstructor;
import nyang.cat.filter.JwtFilter;
import nyang.cat.jwt.JwtTokenProvider;
import nyang.cat.jwt.RefreshTokenRepository;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/* JwtToken 과 JwtFilter 를 SecurityConfig 에 적용하기 위함 */
@RequiredArgsConstructor
public class JwtSecurityConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
        private final JwtTokenProvider jwtTokenProvider;
        private final RefreshTokenRepository refreshTokenRepository;

        /* JwtToken 을 주입받아 security 로직에 JwtFilter 등록 */
    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(jwtTokenProvider, refreshTokenRepository);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}