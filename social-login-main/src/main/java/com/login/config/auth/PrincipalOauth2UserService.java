package com.login.config.auth;

import com.login.config.auth.provider.NaverUserInfo;
import com.login.config.auth.provider.OAuth2UserInfo;
import com.login.model.User;
import com.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * OAuth2UserService를 확장하여 OAuth2 로그인 프로세스에서 사용자 정보를 로드하고 처리합니다.
 * 이 클래스는 네이버 로그인을 통해 얻은 사용자 정보를 관리합니다.
 */
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    /**
     * OAuth2 로그인 요청을 받아 사용자 정보를 로드합니다.
     * @param userRequest OAuth2 로그인 요청 정보
     * @return OAuth2User 로드된 사용자 정보
     * @throws OAuth2AuthenticationException 인증 과정 중 오류 발생 시
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 지원하는 로그인 제공자가 네이버인지 확인
        if (!"naver".equals(userRequest.getClientRegistration().getRegistrationId())) {
            throw new OAuth2AuthenticationException("Only Naver login supported");
        }

        // 네이버 사용자 정보를 가져옴
        OAuth2UserInfo naverUserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        String provider = naverUserInfo.getProvider();
        String providerId = naverUserInfo.getProviderId();
        
        // 사용자 정보를 조회하거나 새로운 사용자를 등록
        User user = userRepository.findByProviderAndProviderId(provider, providerId)
                                  .orElseGet(() -> registerNewUser(naverUserInfo, oAuth2User.getAttributes()));
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

    /**
     * 새 사용자를 등록합니다.
     * @param userInfo OAuth2 사용자 정보
     * @param attributes 사용자의 추가 속성
     * @return User 등록된 사용자
     */
    private User registerNewUser(OAuth2UserInfo userInfo, Map<String, Object> attributes) {
        User user = User.builder()
                        .username("naver_" + userInfo.getProviderId())
                        .password(bCryptPasswordEncoder.encode("defaultPassword"))
                        .email(userInfo.getEmail())
                        .role("ROLE_USER")
                        .provider(userInfo.getProvider())
                        .providerId(userInfo.getProviderId())
                        .build();
        userRepository.save(user);
        return user;
    }
}
