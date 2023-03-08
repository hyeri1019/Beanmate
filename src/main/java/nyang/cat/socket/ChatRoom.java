package nyang.cat.socket;

import lombok.Getter;
import lombok.Setter;
import nyang.cat.Users.entity.Users;
import nyang.cat.product.Product;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Users admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Users customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messageList = new ArrayList<>();

    public ChatRoom(Users admin, Users customer, Product product, List<ChatMessage> messageList) {
        this.admin = admin;
        this.customer = customer;
        this.product = product;
        this.messageList = messageList;
    }

    public ChatRoom() {

    }
}


