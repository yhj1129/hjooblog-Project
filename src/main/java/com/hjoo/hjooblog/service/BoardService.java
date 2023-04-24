package com.hjoo.hjooblog.service;

import com.hjoo.hjooblog.core.exception.ssr.Exception400;
import com.hjoo.hjooblog.core.util.MyParseUtil;
import com.hjoo.hjooblog.dto.board.BoardRequest;
import com.hjoo.hjooblog.model.board.Board;
import com.hjoo.hjooblog.model.board.BoardQueryRepository;
import com.hjoo.hjooblog.model.board.BoardRepository;
import com.hjoo.hjooblog.model.user.User;
import com.hjoo.hjooblog.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardQueryRepository boardQueryRepository;
    @Transactional
    public void 글쓰기(BoardRequest.SaveInDTO saveInDTO, Long userId){
        try {

            // id로 실제 유저가 db에 있는지 확인하고 꺼내옴 (영속화된 유저)
            //1. 유저 존재 확인
            User userPS = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다. "));
            //2. 썸네일 만들기
            String thumbnail = MyParseUtil.getThumbnail(saveInDTO.getContent());
            //3. 게시글 쓰기
            boardRepository.save(saveInDTO.toEntity(userPS, thumbnail));
        } catch (RuntimeException e) {
            throw new RuntimeException("글쓰기 실패 : " + e.getMessage());
        }
    }

    @Transactional(readOnly = true) // 변경 감지 하지 말것, 고립성 (repeatable read)
    public Page<Board> 글목록보기(int  page) { //CSR은 DTO로 변경해서 돌려줘야함
        //1. 모든 전략은 Lazy : 이유는 필요할때만 가져오려고
        //2. 필요할때는 직접 fetch join으로 가져옴
        Page<Board> boardPGPS = boardQueryRepository.findAll(page);
        return boardPGPS;
    }

    public Board 게시글상세보기(Long id) {
        Board boardPS = boardRepository.findByIdFetchUser(id).orElseThrow(
                ()-> new Exception400("id", "게시글 아이디를 찾을 수 없습니다")
        );
        // 1. Lazy Loading 하는 것 보다 join fetch 하는 것이 좋다.
        // 2. 왜 Lazy를 쓰냐면, 쓸데 없는 조인 쿼리를 줄이기 위해서이다.
        // 3. 사실 @ManyToOne은 Eager 전략을 쓰는 것이 좋다.
        // boardPS.getUser().getUsername();
        return boardPS;
    }
}
