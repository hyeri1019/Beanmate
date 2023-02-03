package nyang.cat.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Pagination {

    private int totalPages;
    private int startPage;
    private int endPage;
    private boolean hasNext;
    private boolean hasPrev;
    private int prevIndex;
    private int nextIndex;

    public Pagination(int totalPages, int startPage, int endPage, boolean hasNext, boolean hasPrev, int prevIndex, int nextIndex) {
        this.totalPages = totalPages;
        this.startPage = startPage;
        this.endPage = endPage;
        this.hasNext = hasNext;
        this.hasPrev = hasPrev;
        this.prevIndex = prevIndex;
        this.nextIndex = nextIndex;
    }
}
