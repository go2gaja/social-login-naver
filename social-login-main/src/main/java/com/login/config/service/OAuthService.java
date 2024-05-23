package com.login.config.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuthService {

    public String getNaverAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "Kr0loJLYGdV2Sw9MJQxX");
        params.add("client_secret", "xt64zt7H_H");
        params.add("redirect_uri", "http://localhost:80/oauth2/naver/login/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "https://nid.naver.com/oauth2.0/token", HttpMethod.POST, requestEntity, String.class);

        return response.getBody();  // 이 부분에서 토큰을 반환받음
    }
}
