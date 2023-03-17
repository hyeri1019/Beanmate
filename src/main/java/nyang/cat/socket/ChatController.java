package nyang.cat.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping(value = "/chat/in") /* 클라이언트 요청 : /app/chat/in */
    @SendTo("/topic/message")
    public String sendGreeting(ChatMessage message) throws Exception {
        Thread.sleep(1000);
       return message.getSender().getName()+"님이 입장하셨습니다.";
    }

    @MessageMapping(value = "/chatting") /* 클라이언트 요청 : /app/chatting */
    @SendTo("/topic/message")
    public String sendMessage(ChatMessage message) throws Exception {
        Thread.sleep(1000);

        chatService.save(message);

        return message.getSender().getName()+"*"+message.getMessage();
    }
}

