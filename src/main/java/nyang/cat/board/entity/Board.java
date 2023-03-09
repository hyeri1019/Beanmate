package nyang.cat.board.entity;

import lombok.*;
import nyang.cat.Users.entity.Users;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String category;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @ManyToOne
    @JoinColumn(name="user_seq")
    private Users user;

    private String writer;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Column(nullable=true)
    private int viewCnt;   // 조회수

    @Column(nullable=true)
    private int replyCnt;  // 댓글수

    private Date regTime; // 등록시간

    @Column(nullable=true)
    private String imageName;

    @Builder
    public Board(String title, Users user, String writer, String content, String category) {
        this.title = title;
        this.user = user;
        this.writer = writer;
        this.content = content;
        this.category = category;
    }

    public Board(List<Board> content) {

    }
}
