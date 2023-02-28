package nyang.cat.configuration;

import lombok.RequiredArgsConstructor;
import nyang.cat.jwt.JwtTokenProvider;
import nyang.cat.jwt.RefreshTokenRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import java.util.List;



/*
   -------------스프링 시큐리티 관련 설정 클래스 ---------------
   본래는 WebSecurityConfigurerAdapter 를 상속하여 설정했으나, 더 이상 지원하지 않음
   -> SecurityFilterChain 를 빈으로 등록하는 방식으로 변경
*/

@Configuration // 설정클래스 빈 등록 어노테이션
@RequiredArgsConstructor
@EnableAsync
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public InitializingBean initializingBean() {
        return () -> SecurityContextHolder.setStrategyName(
                SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // ------ 해당 경로의 리소스들은 인증 없이 접근 허용
        return (web) -> web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .exceptionHandling()

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
            /* 기본 세션 -> stateless */
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/auth/**", "/boards","/board/**","/uploads/**","/feeds/**"
                            , "/products/**").permitAll()
//                .antMatchers("/me").hasAnyAuthority("ROLE_USER")

                .anyRequest().authenticated()

                /* JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용 */
                .and()
                .apply(new JwtSecurityConfiguration(jwtTokenProvider,refreshTokenRepository));

        return http.build();
    }

}
