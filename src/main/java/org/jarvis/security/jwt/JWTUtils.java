package org.jarvis.security.jwt;

import com.auth0.jwt.interfaces.Claim;

import java.util.Map;

/**
 * <b>各类JWT库(java)的使用与评价</b><br>
 * <p>//https://andaily.com/blog/?p=956</p>
 */
public class JWTUtils {

    private static JWTService jwtService = new HS256JWTService();

    public static void register(JWTService jwtService) {
        JWTUtils.jwtService = jwtService;
    }

    public static String createToken(Object userInfo) {
        return jwtService.createToken(userInfo);
    }

    public static Map<String, Claim> verifyToken(String token) {
        return jwtService.verifyToken(token);
    }

    public static <T> T getUserInfo(String token, Class<T> classValue) {
        return jwtService.getUserInfo(token, classValue);
    }

}
