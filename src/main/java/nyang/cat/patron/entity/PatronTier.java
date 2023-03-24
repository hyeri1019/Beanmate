package nyang.cat.patron.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PatronTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patron_tier_id")
    private Long id;

    /* N 티어 : 1 크리에이터*/
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Creator creator;

    private String tier;

    private Long amount;

    private String benefits;

    @JsonIgnore
    @OneToMany(mappedBy = "patronTier")
    @ToString.Exclude
    private List<Subscription> subscriptions;

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Builder
    public PatronTier(Creator creator, String tier, Long amount, String benefits) {
        this.creator = creator;
        this.tier = tier;
        this.amount = amount;
        this.benefits = benefits;
    }
}
