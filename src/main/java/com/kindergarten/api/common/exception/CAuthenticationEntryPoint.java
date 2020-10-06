package com.kindergarten.api.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindergarten.api.common.result.CommonResult;
import com.kindergarten.api.common.result.ResponseService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(402);
        response.setContentType("application/json;charset=utf-8");

//        CommonResult responsemsg = new ResponseService().getFailResult(402, "로그인이 되지 않은 사용자 입니다");
//        PrintWriter out = response.getWriter();
//        String jsonResponse = objectMapper.writeValueAsString(responsemsg);
//        out.print(jsonResponse);
    }
}
