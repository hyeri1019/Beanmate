package nyang.cat.socket;

import nyang.cat.Users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>  {

    Optional<ChatRoom> findBySenderAndReceiver(Users sender, Users receiver);

}
