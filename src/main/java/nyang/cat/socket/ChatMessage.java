package nyang.cat.socket;

import lombok.*;
import nyang.cat.Users.entity.Users;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@DynamicInsert
@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_seq")
    private Long id;

    /* Users 실제 사용 시에만 로딩 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_user_id")
    private Users sender;

    private String message;


    @Builder
    public ChatMessage(Users sender, String message) {
        this.sender = sender;
        this.message = message;
    }

}
