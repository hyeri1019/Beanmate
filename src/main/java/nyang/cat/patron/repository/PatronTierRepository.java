package nyang.cat.patron.repository;

import nyang.cat.patron.entity.Creator;
import nyang.cat.patron.entity.PatronTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatronTierRepository extends JpaRepository<PatronTier, Long> {
    List<PatronTier> findByCreator(Creator creator);
    Optional<PatronTier> findByCreatorAndTier(Creator creator, String tier);
}
