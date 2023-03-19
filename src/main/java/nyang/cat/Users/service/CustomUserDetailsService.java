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
import java.util.stream.Collectors;

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


    /* 1 user : N role */
    private UserDetails createUserDetails(Users users) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(users.getAuthority().toString());

        if (users.getSubscriptions().isEmpty()) {
            /*  User 클래스로 사용자 정의(이름,암호,권한)  */
            return new User(
                    String.valueOf(users.getUserSeq()),
                    users.getPassword(),
                    Collections.singleton(grantedAuthority));
        }

        for (Subscription subscription : users.getSubscriptions()) {
            /* [CreatorName_Tier] 등급 추출 */
            String tierName = subscription.getPatronTier().getTier();
            String[] parts = tierName.split("(?<=_)(?=\\d)");
            String creatorName = parts[0];
            int tierLevel = Integer.parseInt(parts[1]);
            System.out.println("tierLevel = " + tierLevel);

            for (int i = 1; i <= tierLevel; i++) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + creatorName + i));
            }
        }

        System.out.println("----------------------grantedAuthorities = " + grantedAuthorities);
        return new User(
                String.valueOf(users.getUserSeq()),
                users.getPassword(),
                grantedAuthorities);
    }


    public UserDetails updateUserDetails(Users users, UserDetails userDetails) {
        Set<GrantedAuthority> grantedAuthorities = userDetails.getAuthorities().stream().collect(Collectors.toSet());

        for (Subscription subscription : users.getSubscriptions()) {

            String tierName = subscription.getPatronTier().getTier();
            String[] parts = tierName.split("(?<=_)(?=\\d)");
            String creatorName = parts[0];
            int tierLevel = Integer.parseInt(parts[1]);

            for (int i = 1; i <= tierLevel; i++) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + creatorName + i));
            }
        }
        System.out.println("구독 완료 / grantedAuthorities 업데이트  = " + grantedAuthorities);
        return new User(userDetails.getUsername(), userDetails.getPassword(), grantedAuthorities);
    }
}