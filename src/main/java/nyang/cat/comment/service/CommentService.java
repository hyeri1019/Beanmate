package nyang.cat.comment.service;

import lombok.RequiredArgsConstructor;
import nyang.cat.board.dto.Pagination;
import nyang.cat.comment.entity.Comment;
import nyang.cat.comment.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Map<String, Object> getCommentList(Pageable pageable, int pageNo, Long pno) {
        Map map = new HashMap();

        Page<Comment> commentList = null;
        List<Comment> listContent = null;

        commentList = commentRepository.findByPno(pageable, pno);
        listContent = commentList.getContent();

        map.put("comments", listContent);

        int totalPage = commentList.getTotalPages();
        int startPage = (int) ((Math.floor(pageNo / 11) * 10) + 1
                <= totalPage ? (Math.floor(pageNo / 11) * 10) + 1 : totalPage);

        int endPage = (startPage + 5 - 1 < totalPage ? startPage + 5 - 1 : totalPage);
        System.out.println("endPage = " + endPage);

        boolean hasPrev = commentList.hasPrevious();
        boolean hasNext = commentList.hasNext();

        int prevIndex = commentList.previousOrFirstPageable().getPageNumber() + 1;
        int nextIndex = commentList.nextOrLastPageable().getPageNumber() + 1;

        map.put("commentsPagination", new Pagination(totalPage, startPage, endPage, hasPrev, hasNext, prevIndex, nextIndex));
        System.out.println(" 댓글 페이지네이션=" + map.get("commentsPagination"));

        return map;
    }
}
