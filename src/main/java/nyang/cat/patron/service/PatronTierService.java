package nyang.cat.patron.service;

import lombok.RequiredArgsConstructor;
import nyang.cat.Users.entity.Users;
import nyang.cat.Users.service.UsersService;
import nyang.cat.patron.entity.Creator;
import nyang.cat.patron.entity.PatronTier;
import nyang.cat.patron.repository.CreatorRepository;
import nyang.cat.patron.repository.PatronTierRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatronTierService {

    private final PatronTierRepository patronTierRepository;
    private final CreatorRepository creatorRepository;
    private final CreatorService creatorService;
    private final UsersService usersService;

    public void registerTier(List<PatronTier> tiers, Authentication authentication) {
        Users user = usersService.getUserInfo(authentication);
        tiers.forEach(tier -> {

                    PatronTier patronTier = PatronTier.builder()
                            .creator(user.getCreator())
                            .tier(user.getName()+"_"+tier.getTier())
                            .amount(tier.getAmount())
                            .benefits(tier.getBenefits())
                            .subscriptions(tier.getSubscriptions())
                            .build();

                    patronTierRepository.save(patronTier);
                });
    }

    public List<PatronTier> showTiers(String creator) {
        Creator findCreator = creatorRepository.findByName(creator).get();
        return patronTierRepository.findByCreator(findCreator);
    }
}
