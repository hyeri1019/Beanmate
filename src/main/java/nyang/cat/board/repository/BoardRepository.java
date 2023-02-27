package nyang.cat.board.repository;

import nyang.cat.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByCategory(String category, Pageable pageable);
    /*  Containing == Like */
    Page<Board> findByTitleContaining(String keyword, Pageable pageable);
    Page<Board> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword, Pageable pageable);
    Page<Board> findByWriterContaining(String writerKeyword, Pageable pageable);



}


