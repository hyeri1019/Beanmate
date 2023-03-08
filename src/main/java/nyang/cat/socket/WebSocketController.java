package nyang.cat.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping(value = "/chat/in") /* 클라이언트 요청 : /app/chat/in */
    @SendTo("/topic/message")
    public String sendGreeting(ChatMessage message) throws Exception {
        Thread.sleep(1000);
       return message.getSender().getEmail()+"님이 입장하셨습니다.*";
    }

    @MessageMapping(value = "/chatting") /* 클라이언트 요청 : /app/chatting */
    @SendTo("/topic/message")
    public String sendMessage(ChatMessage message) throws Exception {
        Thread.sleep(1000);
        return message.getSender().getEmail()+"*"+message.getMessage();
    }
}

