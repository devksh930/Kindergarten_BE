package com.kindergarten.api.security.util;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@Service
public class CookieUtil {

    public Cookie createCookie(String cookieName, String value) {
        Cookie token = new Cookie(cookieName, value);
        token.setHttpOnly(false);
        token.setPath("http://mommyyogi.com");
//        token.setHttpOnly(true);
        token.setMaxAge((int) JwtUtil.TOKEN_VALIDATION_SECOND);
//        token.setPath("/");
        return token;
    }

    public Cookie getCookie(HttpServletRequest request, String cookieName) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie;
            }
        }
        return null;
    }
}
