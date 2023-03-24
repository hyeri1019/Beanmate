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

        String auth = String.valueOf(grantedAuthority);
        System.out.println("auth = " + auth);

        /* 크리에이터는 본인 권한 부여 */
        if (auth.equals("Authority.ROLE_CREATOR")) {
            for (int i = 1; i <= 3; i++) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + users.getName() + "_" + i));
            }
            grantedAuthorities.add(grantedAuthority);
        }

        /* 구독 목록에서 등급 확인 및 권한 부여 */
        if (users.getSubscriptions().isEmpty()) {
            grantedAuthorities.add(grantedAuthority);
            return new User(
                    String.valueOf(users.getUserSeq()),
                    users.getPassword(),
                    grantedAuthorities);
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

            return new User(
                    String.valueOf(users.getUserSeq()),
                    users.getPassword(),
                    grantedAuthorities);
        }
    }