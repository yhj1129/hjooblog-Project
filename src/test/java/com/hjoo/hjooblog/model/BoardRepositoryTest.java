package com.hjoo.hjooblog.model;

import com.hjoo.hjooblog.DummyEntity;
import com.hjoo.hjooblog.model.board.Board;
import com.hjoo.hjooblog.model.board.BoardRepository;
import com.hjoo.hjooblog.model.user.User;
import com.hjoo.hjooblog.model.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
@Import(BCryptPasswordEncoder.class)
@DataJpaTest
public class BoardRepositoryTest extends DummyEntity {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;
    private void join_good(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        List<User> userList = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            userList.add(newUser("user"+i, passwordEncoder));
        }
        userRepository.saveAll(userList);
        List<Board> boardList = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            boardList.add(newBoard("제목"+i, userList.get(i-1)));
        }
        boardRepository.saveAll(boardList);
        em.clear();
    }
    private void in_query_good(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        List<User> userList = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            userList.add(newUser("user"+i, passwordEncoder));
        }
        userRepository.saveAll(userList);
        List<Board> boardList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            boardList.add(newBoard("제목"+i, userList.get(0)));
        }
        for (int i = 5; i < 9; i++) {
            boardList.add(newBoard("제목"+i, userList.get(1)));
        }
        for (int i = 9; i < 11; i++) {
            boardList.add(newBoard("제목"+i, userList.get(2)));
        }
        boardRepository.saveAll(boardList);
        em.clear();
    }
    @BeforeEach
    public void setUp(){
        join_good();
    }

    public void findByUserIds_test(){
        List<Long> userIds = boardRepository.findUserIdDistinct();
        boardRepository.findByUserIds(userIds);
    }
    @Test
    public void findAll_test(){
        // Lazy 전략 -> Board만 조회
        boardRepository.findAll();
    }
    @Test
    public void findAllV2_test(){
        // Lazy 전략 -> Board만 조회
        List<Board> boardList = boardRepository.findAll();
        // Lazy Loading 하기 (20바퀴)
        boardList.stream().forEach(board -> {
            System.out.println(board.getUser().getUsername()); // select 2번
        });
    }
    @Test
    public void findByUserId_test(){
        List<Board> boardList = boardRepository.findByUserId(1L);
        boardList.stream().forEach(board -> {
            // loop 1번에 select  (ssar)
            // loop 2~10번 1차 캐싱 (PC)
            // loop 11번에 select  (cos)
            // loop 11~20번 1차 캐싱 (PC)
            System.out.println(board.getUser().getUsername()); // select 2번
        });
    }
}
