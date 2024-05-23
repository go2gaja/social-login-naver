package com.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OAuthProfile {
    private Long id; // 네이버에서 제공하는 사용자의 고유 ID
    private String name; // 사용자의 이름
    private String email; // 사용자의 이메일 주소
    @JsonProperty("profile_image")
    private String profileImage; // 사용자의 프로필 이미지 URL
    @JsonProperty("age")
    private String age; // 사용자의 연령대
    @JsonProperty("gender")
    private String gender; // 사용자의 성별
    @JsonProperty("birthday")
    private String birthday; // 사용자의 생일
    @JsonProperty("mobile")
    private String mobile; // 사용자의 휴대폰 번호

    // 네이버 API가 제공할 수 있는 추가적인 사용자 정보를 여기에 포함시킬 수 있습니다.
}
