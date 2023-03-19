package nyang.cat.patron.controller;

import lombok.RequiredArgsConstructor;
import nyang.cat.patron.entity.PatronTier;
import nyang.cat.patron.service.PatronTierService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PatronTierController {

    private final PatronTierService patronTierService;

    @GetMapping("/tier")
    public List<PatronTier> showTiers(@RequestParam(required = true, value = "creator") String creator) {
        return patronTierService.showTiers(creator);
    }

    @PostMapping("/tier")
    public void registerTier(@RequestBody List<PatronTier> tier, Authentication authentication) {
        System.out.println("tier = " + tier);
        patronTierService.registerTier(tier, authentication);
    }


}
