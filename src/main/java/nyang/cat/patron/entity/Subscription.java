package nyang.cat.patron.entity;

import lombok.*;
import nyang.cat.Users.entity.Users;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "patron_tier_id")
    private PatronTier patronTier;

//    private LocalDateTime subscriptionStartDate;
//    private LocalDateTime subscriptionEndDate;


    @Builder
    public Subscription(Users user, PatronTier patronTier) {
        this.user = user;
        this.patronTier = patronTier;
    }
}