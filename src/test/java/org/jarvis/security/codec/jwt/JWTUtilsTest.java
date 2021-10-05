package org.jarvis.security.codec.jwt;

import com.auth0.jwt.interfaces.Claim;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jarvis.security.jwt.HS256JWTService;
import org.jarvis.security.jwt.JWTUtils;
import org.jarvis.security.jwt.RS256JWTService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;

class JWTUtilsTest {

    @Test
    void verifyToken_HS256() {
        JWTUtils.register(new HS256JWTService());

        UserInfo userInfo = getUserInfo();
        String token = JWTUtils.createToken(userInfo);
        System.out.println(token);
        Map<String, Claim> claimMap = JWTUtils.verifyToken(token);
        System.out.println(claimMap);
        UserInfo decodeUserInfo = JWTUtils.getUserInfo(token, UserInfo.class);
        Assertions.assertEquals(userInfo, decodeUserInfo);

    }

    @Test
    void verifyToken_RS256() {
        JWTUtils.register(new RS256JWTService());

        UserInfo userInfo = getUserInfo();
        String token = JWTUtils.createToken(userInfo);
        System.out.println(token);
        Map<String, Claim> claimMap = JWTUtils.verifyToken(token);
        System.out.println(claimMap);
        UserInfo decodeUserInfo = JWTUtils.getUserInfo(token, UserInfo.class);
        Assertions.assertEquals(userInfo, decodeUserInfo);

    }

    private UserInfo getUserInfo() {
        return UserInfo.builder()
                .id(1L)
                .name("张三")
                .age(29)
                .nickName("测试名字")
                .build();

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class UserInfo {
        private Long id;
        private String name;
        private Integer age;
        private String nickName;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserInfo userInfo = (UserInfo) o;
            return Objects.equals(id, userInfo.id) && Objects.equals(name, userInfo.name) && Objects.equals(age, userInfo.age) && Objects.equals(nickName, userInfo.nickName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, age, nickName);
        }
    }

}