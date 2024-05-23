package com.login.controller;

import com.login.config.auth.PrincipalDetails;
import com.login.model.User;
import com.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 기본 로그인과 OAuth 로그인을 처리하는 컨트롤러입니다.
 * 이 컨트롤러는 메인 페이지와 로그인 관련 기능을 담당합니다.
 */
@Controller
public class IndexController implements WebMvcConfigurer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/social/login")
    public @ResponseBody String testLogin(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return "일반 로그인 세션 정보 확인: " + principalDetails.getUser();
    }

    @GetMapping("/social/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication) {
        OAuth2User oauth = (OAuth2User) authentication.getPrincipal();
        return "OAuth 로그인 세션 정보 확인: " + oauth.getAttributes();
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return "사용자 정보: " + principalDetails.getUser();
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "관리자 페이지";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "매니저 페이지";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/joinForm")
    public String join(User user) {
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보 페이지";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터 정보 페이지";
    }

    /**
     * 로그인 폼 페이지로 이동합니다.
     * 로그인을 처리하는 별도의 LoginController가 있을 경우, 해당 경로로 리다이렉트 됩니다.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
