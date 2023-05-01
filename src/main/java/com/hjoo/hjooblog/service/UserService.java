package com.hjoo.hjooblog.service;

import com.hjoo.hjooblog.core.exception.ssr.Exception400;
import com.hjoo.hjooblog.core.exception.ssr.Exception500;
import com.hjoo.hjooblog.core.util.MyFileUtil;
import com.hjoo.hjooblog.dto.user.UserRequest;
import com.hjoo.hjooblog.model.user.User;
import com.hjoo.hjooblog.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // 빈 등록함

    @Value("${file.path}")
    private String uploadFolder; // dev.yml의 file 위치

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

    public User 회원프로필보기(Long id) {
        User userPS = userRepository.findById(id).orElseThrow(() -> new Exception400("id", "해당 유저가 존재하지 않습니다. "));
        return userPS;
    }

    @Transactional
    public User 프로필사진수정(MultipartFile profile, Long id) {
        try {
            String uuidImageName = MyFileUtil.write(uploadFolder, profile);


            User userPS = userRepository.findById(id)
                    .orElseThrow(() -> new Exception500("로그인된 유저가 DB에 존재하지 않습니다. "));
            userPS.changeProfile(uuidImageName);
            return userPS;
        }catch (Exception e){
            throw new Exception500("프로필 사진 등록 실패 : " + e.getMessage());
        }
    } //더티체킹
}
