package nyang.cat.board.entity;

import lombok.*;
import nyang.cat.Users.entity.Users;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String category;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="user_seq")
    private Users user;

    @OneToMany(mappedBy = "pno", cascade = CascadeType.REFRESH) /* Likes 의 Board.pno 참조 */
    private List<Likes> likesList;

    private String writer;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Column(nullable=true)
    private int viewCnt;   // 조회수

    @Column(nullable=true)
    private int replyCnt;  // 댓글수

    @Column(nullable=true)
    private int likeCnt;

    @Column(nullable=true)
    private String imageName;

    private String authLevel;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @Builder
    public Board(String title, Users user, String writer, String content, String category, int likeCnt) {
        this.title = title;
        this.user = user;
        this.writer = writer;
        this.content = content;
        this.category = category;
        this.likeCnt = likeCnt;
    }

}
