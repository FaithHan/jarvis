package org.jarvis.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public abstract class CookieUtils {

    /**
     * 根据cookieName获取CookieValue
     *
     * @param httpServletRequest
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest httpServletRequest, String cookieName) {
        return Optional.ofNullable(getCookie(httpServletRequest, cookieName)).map(Cookie::getValue).orElse(null);
    }

    /**
     * 根据名称查找cookie
     *
     * @param httpServletRequest
     * @param cookieName
     * @return
     */
    public static Cookie getCookie(HttpServletRequest httpServletRequest, String cookieName) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName != null && cookieName.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    public static void addCookie(HttpServletResponse httpServletResponse, String name, String value, int maxAge) {
        addCookie(httpServletResponse, name, value, maxAge, true);
    }

    public static void addCookie(HttpServletResponse httpServletResponse, String name, String value, int maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        /* is visible to js or not */
        cookie.setHttpOnly(httpOnly);
        httpServletResponse.addCookie(cookie);
    }

    /**
     * 删除cookie, maxAge=0会立即删除cookie
     *
     * @param httpServletResponse
     * @param name
     */
    public static void deleteCookie(HttpServletResponse httpServletResponse, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
    }

}
