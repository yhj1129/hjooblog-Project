package com.hjoo.hjooblog.controller;

import com.hjoo.hjooblog.core.auth.MyUserDetails;
import com.hjoo.hjooblog.dto.board.BoardRequest;
import com.hjoo.hjooblog.model.board.Board;
import com.hjoo.hjooblog.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Slf4j
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    // RestAPI 주소 설계 규칙에서 자원에는 복수를 붙인다. boards가 정석
    @GetMapping({"/", "/board"})
    public String  main(@RequestParam(defaultValue = "0") Integer page, Model model){
        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by("id").descending());
        Page<Board> boardPG = boardService.글목록보기(pageRequest);
        //return "board/main";
        model.addAttribute("boardPG", boardPG);
        return "board/main";
    }

    @GetMapping("/s/board/saveForm")
    public String saveForm(){
        return "board/saveForm";
    }

    @PostMapping("/s/board/save")
    public String save(BoardRequest.SaveInDTO saveInDTO, @AuthenticationPrincipal MyUserDetails myUserDetails){
        boardService.글쓰기(saveInDTO, myUserDetails.getUser().getId());
        return "redirect:/";
    }
}
