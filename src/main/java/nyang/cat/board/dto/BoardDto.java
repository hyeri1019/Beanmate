package nyang.cat.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nyang.cat.board.entity.Board;
import org.springframework.data.domain.Page;

import java.util.Date;

@Getter
@Setter

public class BoardDto {

    private Long pno;
    private String title;
    private String writer;
    private String content;
    private int viewCnt;
    private int replyCnt;
    private Date regTime;

    @Builder
    public BoardDto(Long pno, String title, String writer, String content, int viewCnt, int replyCnt, Date regTime) {
        this.pno = pno;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.viewCnt = viewCnt;
        this.replyCnt = replyCnt;
        this.regTime = regTime;
    }
    public BoardDto() {}

   public BoardDto(Board board) {
        this.pno = board.getPno();
        this.title = board.getTitle();
        this.writer = board.getWriter();
        this.content = board.getContent();
        this.viewCnt = board.getViewCnt();
        this.replyCnt = board.getReplyCnt();
        this.regTime = board.getRegTime();
   }
}
