package nyang.cat.socket;

import lombok.RequiredArgsConstructor;
import nyang.cat.Users.entity.Users;
import nyang.cat.Users.repository.UsersRepository;
import nyang.cat.Users.service.UsersService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UsersRepository usersRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UsersService usersService;

    @MessageMapping(value = "/chat/{receiverName}") /* 클라이언트 요청 : /app/chat/** */
    public void sendMessageToCreator(@DestinationVariable String receiverName, ChatDTO chatDTO) throws Exception {

        Users receiver =  usersRepository.findByName(receiverName).orElse(null);
        System.out.println("받는 사람 ======================== " + receiver.getName());

        System.out.println("chatDTO = " + chatDTO.getMessage());
        System.out.println("chatDTO = " + chatDTO.getReceiver());
        System.out.println("chatDTO = " + chatDTO.getSender());

        Users sender = usersRepository.findByName(chatDTO.getSender()).orElse(null);

        /* 특정 sender 와 receiver 의 채팅방이 없을 경우에만 생성 */
        ChatRoom chatRoom = chatRoomRepository.findBySenderAndReceiver(sender, receiver)
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder()
                                                        .sender(sender)
                                                        .receiver(receiver)
                                                        .build()));

        ChatMessage chatMessage = ChatMessage.builder()
                .message(chatDTO.getMessage())
                .chatRoom(chatRoom)
                .sender(sender.getName())
                .receiver(receiverName)
                .build();

        chatMessageRepository.save(chatMessage);


        if(receiver == null) {
            throw new UsernameNotFoundException("User "+receiverName+" not found");
        }


        /* message 를 수신자의 topic 으로 전송함 */
        String topic = "/topic/"+receiver.getName();
        simpMessagingTemplate.convertAndSend(topic, chatDTO);
    }

//    @GetMapping("/chat")
//    public Map<String, Object> showChatMessages(@RequestParam String receiver, Authentication authentication) {
//        System.out.println(" 채팅 목록 ");
//        Users sender = usersService.getUserInfo(authentication);
//        Users receiver1 = usersRepository.findByName(receiver).orElse(null);
//        chatMessageRepository.findBySender
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("sender", sender);
//
//        System.out.println("receiver = " + receiver);
//        System.out.println("sender = " + sender.getName());
//
//        return map;
//
//    }
}

