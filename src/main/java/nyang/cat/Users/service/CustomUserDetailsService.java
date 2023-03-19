package nyang.cat.Users.service;

import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import nyang.cat.Users.entity.Users;
import nyang.cat.Users.repository.UsersRepository;
import nyang.cat.patron.entity.PatronTier;
import nyang.cat.patron.entity.Subscription;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    @Transactional
    /* login */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(" load by username ");
            return usersRepository.findByEmail(username)
                    .map(this::createUserDetails)
                    .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
        }


    private UserDetails createUserDetails(Users users) {
        Set<GrantedAuthority> grantedAuthorities  = new HashSet<>();

        if(users.getSubscriptions().isEmpty()) {
            System.out.println(" ---------------- empty ");
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(users.getAuthority().toString());
            /*  User 클래스로 사용자 정의(이름,암호,권한)  */
            return new User(
                    String.valueOf(users.getUserSeq()),
                    users.getPassword(),
                    Collections.singleton(grantedAuthority));
        }

        for(Subscription subscription : users.getSubscriptions()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+subscription.getPatronTier().getTier()));
        }

        System.out.println("----------------------grantedAuthorities = " + grantedAuthorities);
        return new User(
                String.valueOf(users.getUserSeq()),
                users.getPassword(),
                grantedAuthorities);
    }
}