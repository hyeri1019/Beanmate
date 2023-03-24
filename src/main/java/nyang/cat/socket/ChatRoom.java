package nyang.cat.socket;

import lombok.*;
import nyang.cat.Users.entity.Users;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Users sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Users receiver;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public void addMessage(ChatMessage message) {
        chatMessages.add(message);
        message.setChatRoom(this);
    }

    @Builder
    public ChatRoom(Users sender, Users receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
}
