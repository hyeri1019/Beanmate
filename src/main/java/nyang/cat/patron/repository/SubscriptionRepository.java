package nyang.cat.patron.repository;

import nyang.cat.Users.entity.Users;
import nyang.cat.board.entity.Board;
import nyang.cat.patron.entity.PatronTier;
import nyang.cat.patron.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserAndPatronTier(Users user, PatronTier patronTier);
}
