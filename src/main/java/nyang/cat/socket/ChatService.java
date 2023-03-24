package nyang.cat.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;

    public void save(ChatMessage message) {
        System.out.println("message = " + message);
        chatMessageRepository.save(message);
    }
}
