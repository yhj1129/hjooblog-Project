package com.hjoo.hjooblog.controller;

import com.hjoo.hjooblog.dto.user.UserRequest;
import com.hjoo.hjooblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    //인증이 되지 않은 상태에서 인증과 관련된 주소는 앞에 엔티티 적지 않는다
    // write (post) : /리소스/식별자(pk, uk 만)/save or delete or update
    // read (get) : /리소스/식별자
    @PostMapping("/join")
    public String join(UserRequest.JoinInDTO joinInDTO){ // x-www-form-urlencoded
        userService.회원가입(joinInDTO);
        return "redirect:/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "user/joinForm";
    }
    @GetMapping("/loginForm")
    public String loginForm(){
        return "user/loginForm";
    }
}
