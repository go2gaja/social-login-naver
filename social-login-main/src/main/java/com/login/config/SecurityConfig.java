package com.login.config;

import com.login.config.auth.PrincipalOauth2UserService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * 스프링 시큐리티 설정을 담당하는 클래스입니다.
 * 소셜 로그인 방식을 지원하며, 인증과 권한 부여를 관리합니다.
 */
@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록됩니다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // 특정 메소드에 대한 접근 보안을 설정합니다.
public class SecurityConfig {
    
    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    /**
     * 스프링 시큐리티 필터 체인을 정의합니다.
     * CSRF 보호를 비활성화하고, 사용자 정의 로그인 페이지를 설정하며,
     * 소셜 로그인 설정을 포함합니다.
     *
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain 객체
     * @throws Exception 필터 체인 구성 중 발생하는 예외를 처리합니다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 보호 기능 비활성화
            .csrf().disable()
            // 요청별 권한 설정
            .authorizeRequests()
                .antMatchers("/user/**").authenticated() // "/user/**" 경로는 인증 필요
                .antMatchers("/manager/**").access("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() // 그 외의 요청은 모두 허용
            .and()
            // 세션 관리 설정
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션 필요 시 생성
            .and()
            // 폼 로그인 설정
            .formLogin()
                .loginPage("/loginForm") // 사용자 정의 로그인 페이지
                .loginProcessingUrl("/loginForm") // 로그인 처리 경로
                .defaultSuccessUrl("/") // 로그인 성공 후 리다이렉션할 기본 URL
            .and()
            // OAuth2 로그인 설정
            .oauth2Login()
                .loginPage("/loginForm") // 소셜 로그인 후 처리 페이지
                .userInfoEndpoint() // 사용자 정보를 가져오기 위한 설정
                .userService(principalOauth2UserService); // OAuth2UserService를 구현한 서비스 연결

        return http.build();
    }
}
