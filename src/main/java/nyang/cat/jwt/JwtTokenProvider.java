package nyang.cat.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import nyang.cat.dto.JwtDto;
import nyang.cat.dto.JwtRequestDto;
import nyang.cat.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.net.http.HttpResponse;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {


    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 2;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private static byte[] keyBytes  = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
    private static Key key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());


    /*  토큰 생성  */
    public static JwtDto generateToken(Authentication authentication) {
        System.out.println(" key= " + key);
        /* 권한 가져오기 */
        String authorities  = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        /*    access Token 생성    */
        Date accessTokenExp = new Date(now+ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExp)
                .signWith(key)
                .compact();
        System.out.println("토큰 발급 : " + accessToken);

        /*    refresh Token 생성    */
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now+REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key)
                .compact();


        return JwtDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExp.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        System.out.println(" 권한 읽기 ");
        /*  토큰 디코딩  */
        Claims claims = parseClaims(accessToken);

        if(claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없습니다.");
        }

        /*   클레임에서 권한정보 가져오기   */
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        System.out.println("authorities = " + authorities);

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal,"",authorities);
    }


    /*  토큰 검증  */
    public int tokenValidation(String token) {
        System.out.println(" 토큰 검증 ");
        try {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return 0;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            e.printStackTrace();
            System.out.println(" sign error ");
            return 1;
        } catch (ExpiredJwtException e) {
            System.out.println(" expired error ");
            return 2;
        } catch (UnsupportedJwtException e) {
            System.out.println(" unSupported error ");
            return 3;
        } catch (IllegalArgumentException e) {
            System.out.println(" Illegal error");
        }
        return 4;
    }


    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
