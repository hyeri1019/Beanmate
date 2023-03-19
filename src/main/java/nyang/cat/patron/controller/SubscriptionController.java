package nyang.cat.patron.controller;

import lombok.RequiredArgsConstructor;
import nyang.cat.patron.service.SubscriptionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscription")
    public void subscriptionCreator(@RequestParam(required = true, value = "creator") String creator,
                                    @RequestParam(required = true, value = "tier") String tier, Authentication authentication) {
        subscriptionService.subscription(creator, tier, authentication);
    }
}
