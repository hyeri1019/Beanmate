package nyang.cat.board.repository;

import nyang.cat.Users.entity.Users;
import nyang.cat.board.entity.Board;
import nyang.cat.board.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByPnoAndUser(Board pno, Users user);
    void deleteByPnoAndUser(Board pno, Users user);
}
