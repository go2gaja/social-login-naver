package com.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.login.config.service.OAuthService;

@RestController
public class OAuthController {

    @Autowired
    private OAuthService oAuthService;

    @GetMapping("/oauth2/callback")
    public String handleOAuthCallback(@RequestParam String code) {
        String accessToken = oAuthService.getNaverAccessToken(code);
        return "인증 완료: " + accessToken;
    }
}
