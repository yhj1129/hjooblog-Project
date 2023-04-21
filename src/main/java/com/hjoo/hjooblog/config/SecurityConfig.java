package com.hjoo.hjooblog.config;

import com.hjoo.hjooblog.core.auth.MyUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpSession;

@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //필터 체인
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. CSRF 해제 (추후에 필요하면 제거)
        http.csrf().disable();

        // 2. frame option 해제
        http.headers().frameOptions().disable();

        //3. Form 로그인 설정
        http.formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // 이 url 타면 MyUserDetailsService 호출되고 Post, x-www로 요청
                .successHandler((request, response, authentication) -> {
                    log.debug("디버그 : 로그인 성공");

                    MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
                    HttpSession session = request.getSession();
                    session.setAttribute("sessionUser", myUserDetails.getUser());

                    response.sendRedirect("/");
                })
                .failureHandler((request, response, exception) -> {
                    log.debug("디버그 : 로그인 실패  : " + exception.getMessage());
                    response.sendRedirect("/loginForm");
                });

        // 3. 인증, 권한 필터 설정
        http.authorizeRequests(
                authorize -> authorize.antMatchers("/s/**").authenticated()
                        .anyRequest().permitAll()
        );
        return http.build();
    }
}
