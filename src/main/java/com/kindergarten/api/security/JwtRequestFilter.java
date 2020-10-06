package com.kindergarten.api.security;

import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.security.util.CookieUtil;
import com.kindergarten.api.security.util.JwtUtil;
import com.kindergarten.api.security.util.RedisUtil;
import com.kindergarten.api.service.Impl.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private RedisUtil redisUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final Cookie jwtToken = cookieUtil.getCookie(request, JwtUtil.ACCESS_TOKEN_NAME);

        String userid = null;
        String jwt = null;
        String refreshJwt = null;
        String refreshUid = null;
        
        try {
            if (jwtToken != null) {
                jwt = jwtToken.getValue();
                userid = jwtUtil.getUserid(jwt);
            }
            if (userid != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userid);

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                   log.debug("=========================");
                   log.debug(userDetails.getUsername());
                   log.debug(userDetails.getAuthorities().toString());
                   log.debug("=========================");
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

        } catch (ExpiredJwtException e) {
            Cookie refreshToken = cookieUtil.getCookie(request, JwtUtil.REFRESH_TOKEN_NAME);
            if (refreshToken != null) {
                refreshJwt = refreshToken.getValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (refreshJwt != null) {
                refreshUid = redisUtil.getData(refreshJwt);

                if (refreshUid.equals(jwtUtil.getUserid(refreshJwt))) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(refreshUid);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    User user = new User();
                    user.setUserid(refreshUid);
                    String newToken = jwtUtil.generateToken(user);

                    Cookie newAccessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, newToken);
                    response.addCookie(newAccessToken);
                }
            }
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }
}
