package nyang.cat.board.dto;

import lombok.*;
import nyang.cat.board.entity.Board;
import org.springframework.data.domain.Page;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardDto {

    private Long pno;
    private String title;
    private String writer;
    private String writerName;
    private String content;
    private int viewCnt;
    private int replyCnt;
    private Date regTime;


}
