// UserStore.java
package com.login.util;

import com.login.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * UserStore 클래스는 애플리케이션의 메모리 내에 사용자 정보를 저장하고 관리하는 임시 저장소를 제공합니다.
 * 이 클래스는 데이터베이스 대신 간단한 사용자 관리를 위한 목적으로 사용될 수 있으며, 테스트 환경에서 유용하게 활용될 수 있습니다.
 */
public class UserStore {
    // 사용자 목록을 저장하는 리스트
    private static final List<User> userList = new ArrayList<>();

    static {
        // 클래스 로딩 시 초기 사용자를 하나 생성하여 리스트에 추가합니다.
        // 이 사용자는 예제로 제공되며, 실제 애플리케이션에서는 사용자 입력에 따라 동적으로 사용자가 추가되어야 합니다.
        userList.add(new User(1, "exampleUser", UUID.randomUUID().toString(), 
                              "93joyko@naver.com", "ROLE_USER", "naver", "1234567890", null));
    }

    /**
     * 새로운 사용자를 userList에 추가합니다.
     * 
     * @param user 추가할 사용자 객체
     */
    public static void save(User user) {
        userList.add(user);
    }

    /**
     * 사용자 이름으로 사용자를 찾아 반환합니다.
     * 
     * @param username 찾고자 하는 사용자의 사용자 이름
     * @return 해당 사용자 이름을 가진 사용자 객체, 없으면 null 반환
     */
    public static User findByUsername(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // 찾고자 하는 사용자가 목록에 없으면 null 반환
    }
}
