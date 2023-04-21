package com.hjoo.hjooblog.service;

import com.hjoo.hjooblog.dto.board.BoardRequest;
import com.hjoo.hjooblog.model.board.BoardRepository;
import com.hjoo.hjooblog.model.user.User;
import com.hjoo.hjooblog.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void 글쓰기(BoardRequest.SaveInDTO saveInDTO, Long userId){
        try {
            // id로 실제 유저가 db에 있는지 확인하고 꺼내옴 (영속화된 유저)
            //1. 유저 존재 확인
            User userPS = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다. "));
            //2. 게시글 쓰기
            boardRepository.save(saveInDTO.toEntity(userPS));
        } catch (RuntimeException e) {
            throw new RuntimeException("글쓰기 실패 : " + e.getMessage());
        }
    }
}
