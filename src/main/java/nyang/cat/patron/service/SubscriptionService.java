package nyang.cat.patron.service;

import lombok.RequiredArgsConstructor;
import nyang.cat.Users.entity.Users;
import nyang.cat.Users.service.UsersService;
import nyang.cat.patron.entity.Creator;
import nyang.cat.patron.entity.PatronTier;
import nyang.cat.patron.entity.Subscription;
import nyang.cat.patron.repository.CreatorRepository;
import nyang.cat.patron.repository.PatronTierRepository;
import nyang.cat.patron.repository.SubscriptionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PatronTierRepository patronTierRepository;
    private final CreatorRepository creatorRepository;
    private final UsersService usersService;

    public void subscription(String creator, String tier, Authentication authentication) {
        Users userInfo = usersService.getUserInfo(authentication);
        Creator getCreator = creatorRepository.findByName(creator).orElse(null);
        PatronTier patronTier = patronTierRepository.findByCreatorAndTier(getCreator, tier).orElse(null);
        System.out.println("patronTier =========== " + patronTier);

        Subscription subscription = Subscription.builder()
                .user(userInfo)
                .patronTier(patronTier)
                .build();
        System.out.println("subscription =============== " + subscription);
        subscriptionRepository.save(subscription);


    }
}
