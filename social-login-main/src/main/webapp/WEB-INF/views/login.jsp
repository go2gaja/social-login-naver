<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>네이버 로그인</title>
    <script type="text/javascript" src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>
</head>
<body>
    <h2>네이버 로그인 페이지</h2>
    <div id="naver_id_login"></div>

    <script>
        var naver_id_login = new naver.LoginWithNaverId({
            clientId: "Kr0loJLYGdV2Sw9MJQxX", // 네이버 클라이언트 ID
            callbackUrl: "http://localhost:80/oauth2/naver/login/callback", // 콜백 URL
            isPopup: false, // 팝업 사용 여부
            loginButton: {color: "green", type: 3, height: 60} // 로그인 버튼 스타일
        });
        naver_id_login.init();

        window.addEventListener('load', function () {
            naver_id_login.getLoginStatus(function (status) {
                if (status) {
                    var email = naver_id_login.user.getEmail();
                    if (!email) {
                        alert("이메일은 필수정보입니다. 정보제공을 동의해주세요.");
                        naver_id_login.reprompt();
                    }
                } else {
                    console.log("로그인되지 않음");
                }
            });
        });
    </script>

    <a href="/oauth2/authorization/naver" style="text-decoration: none;">
        <img src="/images/btnG_축약형.png" alt="네이버 로그인" />
    </a>
</body>
</html>
