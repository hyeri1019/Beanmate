package nyang.cat.board.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bytecode.constant.DefaultValue;
import nyang.cat.Users.entity.Users;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lno;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="user_seq")
    private Users user;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "pno")
    private Board pno;

    private Date regTime;

    @Builder
    public Likes(Users user, Board board) {
        this.user = user;
        this.pno = board;
    }

}
