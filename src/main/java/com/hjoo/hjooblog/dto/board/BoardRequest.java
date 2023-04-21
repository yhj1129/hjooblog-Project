package com.hjoo.hjooblog.dto.board;


import com.hjoo.hjooblog.model.board.Board;
import com.hjoo.hjooblog.model.user.User;
import lombok.Getter;
import lombok.Setter;

public class BoardRequest {

    @Getter @Setter
    public static class SaveInDTO{
        private String title;
        private String content;

        public Board toEntity(User user) {
            return Board.builder()
                    .user(user)
                    .title(title)
                    .content(content)
                    .thumbnail(null)
                    .build();
        }
    }
}
