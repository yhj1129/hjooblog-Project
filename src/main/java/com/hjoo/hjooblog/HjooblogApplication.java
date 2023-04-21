package com.hjoo.hjooblog;

import com.hjoo.hjooblog.model.user.User;
import com.hjoo.hjooblog.model.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class HjooblogApplication {

    @Bean
    CommandLineRunner init(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        return args -> {
            User ssar = User.builder()
                    .username("ssar")
                    .password(passwordEncoder.encode("1234"))
                    .email("ssar@nate.com")
                    .role("USER")
                    .status(true)
                    .profile("person.png")
                    .build();
            userRepository.save(ssar);
        };
    }
    public static void main(String[] args) {
        SpringApplication.run(HjooblogApplication.class, args);
    }

}
