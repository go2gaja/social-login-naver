
package com.login.util;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
public class NaverOAuthController {

    // 네이버 API에서 제공받은 클라이언트 ID와 클라이언트 시크릿을 상수로 설정
    private static final String CLIENT_ID = "Kr0loJLYGdV2Sw9MJQxX";
    private static final String CLIENT_SECRET = "xt64zt7H_H";
    private static final String REDIRECT_URI = "http://localhost:80/oauth2/naver/login/callback";

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/oauth2/naver/callback")
    public ResponseEntity<?> handleNaverOAuth(@RequestParam("code") String code, @RequestParam("state") String state) {
        // 인증 코드를 사용하여 액세스 토큰을 요청
        String accessToken = getAccessToken(code, state);
        if (accessToken == null) {
            // 액세스 토큰 요청 실패 시, 에러 메시지 반환
            return ResponseEntity.badRequest().body("인증 정보가 없습니다.");
        }

        // 액세스 토큰을 사용하여 사용자 정보 조회
        Map<String, Object> userInfo = getUserInfo(accessToken);
        return ResponseEntity.ok(userInfo);
    }

    private String getAccessToken(String code, String state) {
        // 네이버 토큰 요청 URI
        String tokenUri = "https://nid.naver.com/oauth2.0/token";

        // URI를 구성할 때 사용할 UriComponentsBuilder
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tokenUri)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", "Kr0loJLYGdV2Sw9MJQxX")
                .queryParam("client_secret", "xt64zt7H_H")
                .queryParam("redirect_uri", "http://localhost:80/oauth2/naver/login/callback")
                .queryParam("code", code)
                .queryParam("state", state);

        // REST 템플릿을 사용하여 토큰 요청
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(builder.toUriString(), null, Map.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map responseBody = responseEntity.getBody();
            return (String) responseBody.get("access_token");
        }

        return null;
    }

    private Map<String, Object> getUserInfo(String accessToken) {
        // 사용자 정보 요청 URI
        String userInfoUri = "https://openapi.naver.com/v1/nid/me";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        return null;
    }
}
