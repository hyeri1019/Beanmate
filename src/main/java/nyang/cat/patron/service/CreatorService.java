package nyang.cat.patron.service;

import lombok.RequiredArgsConstructor;
import nyang.cat.Users.entity.Users;
import nyang.cat.Users.service.UsersService;
import nyang.cat.board.dto.Pagination;
import nyang.cat.board.dto.SearchHandler;
import nyang.cat.board.entity.Board;
import nyang.cat.board.repository.BoardRepository;
import nyang.cat.patron.entity.Creator;
import nyang.cat.patron.repository.CreatorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreatorService {

    private final CreatorRepository creatorRepository;
    private final BoardRepository boardRepository;
    private final UsersService usersService;


    public Map<String, Object> showCreatorPage(String writer, Pageable pageable, SearchHandler sc, int pageNo) {
        Map map = new HashMap();



        Page<Board> boardList = null;
        List<Board> listContent = null;

        boardList = boardRepository.findByWriter(writer, pageable);
        listContent = boardList.getContent();

        map.put("posts", listContent);

        int totalPage = boardList.getTotalPages();
        int startPage = (int) ((Math.floor((pageNo - 1) / 10) * 10) + 1
                <= totalPage ? (Math.floor((pageNo - 1) / 10) * 10) + 1 : totalPage);

        int endPage = (startPage + 10 - 1 < totalPage ? startPage + 10 - 1 : totalPage);

        boolean hasPrev = boardList.hasPrevious();
        boolean hasNext = boardList.hasNext();

        int prevIndex = boardList.previousOrFirstPageable().getPageNumber() + 1;
        int nextIndex = boardList.nextOrLastPageable().getPageNumber() + 1;

        map.put("pagination", new Pagination(totalPage, startPage, endPage, hasPrev, hasNext, prevIndex, nextIndex));

        return map;
    }



    public void creatorRegister(Creator profile, Authentication authentication) {
        Users user = usersService.getUserInfo(authentication);

        Creator creator = Creator.builder()
                .user(user)
                .about(profile.getAbout())
                .name(user.getName())
                .build();

        creatorRepository.save(creator);
    }

}
