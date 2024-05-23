package com.login.repository;

import com.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository 인터페이스는 Spring Data JPA를 활용하여 사용자 데이터에 대한 CRUD 연산을 제공합니다.
 * 이 인터페이스를 통해 데이터베이스에 대한 복잡한 쿼리 없이도 필요한 데이터를 쉽게 조회하고 관리할 수 있습니다.
 *
 * JpaRepository를 상속받기 때문에 기본적인 데이터베이스 연산과 함께 복잡한 SQL 작성 없이 메서드 이름으로 쿼리를 생성하는 기능을 자동으로 지원합니다.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 사용자 이름을 기반으로 사용자를 조회합니다.
     * 이 메서드는 데이터베이스에서 username에 해당하는 User 객체를 찾아 Optional로 감싸서 반환합니다.
     * 
     * @param username 사용자 이름
     * @return Optional<User> - 사용자 이름에 해당하는 User 객체를 Optional로 감싸서 반환
     */
    Optional<User> findByUsername(String username);

    /**
     * 제공자 이름과 제공자 ID를 기반으로 사용자를 조회합니다.
     * 이 메서드는 데이터베이스에서 해당 제공자와 제공자 ID를 갖는 User 객체를 찾아 Optional로 감싸서 반환합니다.
     * 주로 소셜 로그인에서 사용되는 메서드로, 특정 소셜 미디어 서비스를 통해 로그인한 사용자의 정보를 관리할 때 사용됩니다.
     *
     * @param provider 사용자를 인증한 제공자의 이름 (예: "naver")
     * @param providerId 제공자가 사용자에게 할당한 고유 ID
     * @return Optional<User> - 제공자 이름과 ID에 해당하는 User 객체를 Optional로 감싸서 반환
     */
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
