package nyang.cat.socket;

import nyang.cat.Users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<ChatMessage, Long>  {

}
