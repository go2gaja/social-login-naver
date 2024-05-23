// Fetch.java
package com.login.util;

import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class Fetch {
    private static final RestTemplate restTemplate = new RestTemplate();

    public static ResponseEntity<String> getNaverAccessToken(String url, MultiValueMap<String, String> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    }

    public static ResponseEntity<String> getNaverUserProfile(String url, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
    }
}
