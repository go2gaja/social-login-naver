package com.login.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NaverToken {
    private String accessToken;        // 액세스 토큰
    private String tokenType;          // 토큰 타입 (보통 Bearer)
    private String refreshToken;       // 갱신을 위한 리프레시 토큰
    private int expiresIn;             // 액세스 토큰의 만료 시간 (초 단위)
    private String scope;              // 접근 권한 범위
    private int refreshTokenExpiresIn; // 리프레시 토큰의 만료 시간 (초 단위)
}
