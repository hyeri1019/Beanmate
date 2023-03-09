package nyang.cat.board.service;

import lombok.RequiredArgsConstructor;
import nyang.cat.Users.entity.Users;
import nyang.cat.Users.repository.UsersRepository;
import nyang.cat.board.entity.Board;
import nyang.cat.board.entity.Likes;
import nyang.cat.board.repository.BoardRepository;
import nyang.cat.board.repository.LikesRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikesService {

    private final LikesRepository likesRepository;
    private final BoardService boardService;
    private final BoardRepository boardRepository;


    public boolean isLiked(Long pno, Authentication authentication) {
        Users user = boardService.getUserInfo(authentication);
        Board board = boardRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException(String.valueOf(pno)));
        Optional<Likes> likes = likesRepository.findByPnoAndUser(board, user);

        return likes.isPresent();
    }

    @Transactional
    public Boolean increase(Long pno, Authentication authentication) {
        Users user = boardService.getUserInfo(authentication);
        Board board = boardRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException(String.valueOf(pno)));

        Optional<Likes> isLiked = likesRepository.findByPnoAndUser(board, user);


        if(isLiked.isPresent()) {
            board.setLikeCnt(board.getLikeCnt() - 1);
            boardRepository.save(board);
            likesRepository.deleteByPnoAndUser(board, user);

            return false;
        }

        board.setLikeCnt(board.getLikeCnt() + 1);
        Likes likes = Likes.builder()
                .user(user)
                .pno(board)
                .build();
        boardRepository.save(board);
        likesRepository.save(likes);

        return true;
    }


}
