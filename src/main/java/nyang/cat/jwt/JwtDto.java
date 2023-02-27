package nyang.cat.jwt;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JwtDto {

        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long accessTokenExpiresIn;
    }

