package com.login.config.auth;

import com.login.model.User;
import com.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService를 구현하여 일반 로그인 시 사용자 인증 정보를 로드합니다.
 * 이 서비스는 사용자 이름을 기반으로 데이터베이스에서 사용자 정보를 조회합니다.
 */
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 사용자 이름을 받아 UserDetails를 로드합니다.
     * @param username 사용자 이름
     * @return UserDetails 사용자 인증 정보
     * @throws UsernameNotFoundException 사용자를 찾지 못한 경우 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                                  .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new PrincipalDetails(user);
    }
}
