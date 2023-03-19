package nyang.cat.Users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import nyang.cat.jwt.Authority;
import nyang.cat.patron.entity.Creator;
import nyang.cat.patron.entity.Subscription;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq; // PK

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String name;

    private String password;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Creator creator;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Subscription> subscriptions = new ArrayList<>();

    @Builder
    public Users(String email, String name, String password, Authority authority) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.authority = authority;
    }


}