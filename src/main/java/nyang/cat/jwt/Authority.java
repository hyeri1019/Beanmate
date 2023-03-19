package nyang.cat.jwt;


import lombok.Getter;
import lombok.ToString;

@ToString
public enum Authority {

    /*  스프링 시큐리티 권한 이름 규칙 : "ROLE_"로 시작해야 함  */
        ROLE_USER, ROLE_ADMIN,

}

