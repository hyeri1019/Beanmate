package nyang.cat.board.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String category;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

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

    @Column(nullable = true)
    private String imagePath;

    public Board(String title, String writer, String content) {
        this.title = title;
        this.writer = writer;
        this.content = content;
    }

    public Board(){}

    public Board(List<Board> content) {

    }
}
