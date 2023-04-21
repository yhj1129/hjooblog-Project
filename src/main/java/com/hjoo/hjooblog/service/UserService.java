package com.hjoo.hjooblog.service;

import com.hjoo.hjooblog.dto.user.UserRequest;
import com.hjoo.hjooblog.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // 빈 등록함

    // insert, update, delete -> try - catch 처리
    @Transactional
    public void 회원가입(UserRequest.JoinInDTO joinInDTO){
        try {
            // 1. 패스워드 암호화
            joinInDTO.setPassword(passwordEncoder.encode(joinInDTO.getPassword()));

            // 2. DB 저장
            userRepository.save(joinInDTO.toEntity());
        } catch (Exception e) {
            throw new RuntimeException("회원가입 오류 : " + e.getMessage());
        }
    }// 더티체킹, 디비 세션 종료 (OSIV = false로 정의해둠) 컨트롤러에 레이지 로딩 불가능
}
