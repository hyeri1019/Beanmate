package nyang.cat.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

@Entity
@Getter
@ToString
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq; // PK

    @Column(unique = true)
    private String email;  // UQ

    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Users(Long userSeq, String email, String password, Authority authority) {
        this.userSeq = userSeq;
        this.email = email;
        this.password = password;
        this.authority = authority;
    }

    public Users() {

    }
}