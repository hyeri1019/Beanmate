package nyang.cat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class JwtRequestDto {
    private String accessToken;
    private String refreshToken;
}