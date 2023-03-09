package nyang.cat.Users.entity;

import lombok.*;
import net.bytebuddy.implementation.bytecode.constant.DefaultValue;
import nyang.cat.jwt.Authority;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
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



    @Builder
    public Users(String email, String name, String password, Authority authority) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.authority = authority;
    }

}