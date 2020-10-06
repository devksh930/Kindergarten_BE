package com.kindergarten.api.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindergarten.api.common.result.CommonResult;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.model.entity.UserRole;
import com.kindergarten.api.service.Impl.UserDetailsImpl;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@Component
public class CAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(402);
        response.setContentType("application/json;charset=utf-8");
        CommonResult responsemsg = new ResponseService().getFailResult(402, "접근권한을 가지고 있지 않습니다.");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        Collection<GrantedAuthority> authorities = user.getAuthorities();
//
//        if (hasRole(authorities, UserRole.ROLE_USER.name())) {
//            responsemsg.setMsg("사용자 인증이 안됬지롱");
//        }
        PrintWriter out = response.getWriter();
        String jsonResponse = objectMapper.writeValueAsString(responsemsg);
        out.println(jsonResponse);
    }

    private boolean hasRole(Collection<GrantedAuthority> authorities, String role) {
        return authorities.contains(new SimpleGrantedAuthority(role));
    }
}
