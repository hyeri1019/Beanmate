package nyang.cat.patron.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import nyang.cat.Users.entity.Users;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Creator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creator_id")
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_seq")
    private Users user;

    private String name;

    private String about;

    @Column(nullable=true)
    private String profileBackground;

    @Column(nullable = true)
    private String profileImage;

    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    private List<PatronTier> patronTiers;

    @Builder
    public Creator(Users user, String about, String name) {
        this.user = user;
        this.about = about;
        this.name = name;
    }
}
