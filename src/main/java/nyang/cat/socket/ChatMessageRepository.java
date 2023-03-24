package nyang.cat.socket;

import nyang.cat.Users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>  {
    Optional<ChatMessage> findByReceiver(String receiver);
}
