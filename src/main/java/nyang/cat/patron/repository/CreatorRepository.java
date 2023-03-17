package nyang.cat.patron.repository;

import nyang.cat.Users.entity.Users;
import nyang.cat.patron.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Long> {
    Optional<Creator> findByName(String name);
}
