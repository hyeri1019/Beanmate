package nyang.cat.patron.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatronTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patron_tier_id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Creator creator;

    private String tier;

    private Long amount;

    private String benefits;

    @JsonIgnore
    @OneToMany(mappedBy = "patronTier")
    private List<Subscription> subscriptions;

    @Builder
    public PatronTier(Creator creator, String tier, Long amount, String benefits) {
        this.creator = creator;
        this.tier = tier;
        this.amount = amount;
        this.benefits = benefits;
    }
}
