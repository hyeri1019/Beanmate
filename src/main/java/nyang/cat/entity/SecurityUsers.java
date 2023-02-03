package nyang.cat.entity;

import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
public class SecurityUsers implements UserDetails {

    private final Users users;

    public SecurityUsers(Users users) {
        this.users = users;
    }

    // 사용자권한
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : users.getAuthority().toString().split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }



    @Override    /*  getUsername : 사용자의 id를 반환 ( id는 유니크 값이어야 함 )  */
    public String getUsername() {
        return users.getEmail();
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }


    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        return true;
    }
}
