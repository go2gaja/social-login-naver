package com.login.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.login.model.User;
import com.login.repository.UserRepository;

@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/oauth2/naver/login/callback")
    public ResponseEntity<String> handleOAuth2Callback(@RequestParam(required = false) String code, OAuth2AuthenticationToken authentication) {
        logger.info("Received OAuth2 code: {}", code); // 인증 코드 로그 출력
        if (authentication == null) {
            logger.error("OAuth2AuthenticationToken is null - 인증 토큰이 존재하지 않습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 정보가 없습니다.");
        }

        OAuth2User oauth2User = authentication.getPrincipal();
        String username = oauth2User.getAttribute("id");
        String email = oauth2User.getAttribute("email");

        User user = userRepository.findByUsername(username)
            .map(existingUser -> {
                existingUser.setEmail(email);
                userRepository.save(existingUser);
                logger.info("기존 사용자 정보 업데이트: " + existingUser.getUsername());
                return existingUser;
            })
            .orElseGet(() -> {
                User newUser = User.builder()
                    .username(username)
                    .email(email)
                    .password(bCryptPasswordEncoder.encode("default_password"))
                    .role("ROLE_USER")
                    .build();
                userRepository.save(newUser);
                logger.info("새 사용자 등록: " + newUser.getUsername());
                return newUser;
            });

        logger.info("사용자 로그인 성공: " + username);
        return ResponseEntity.ok("로그인 성공!");
    }
}
