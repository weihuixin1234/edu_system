package com.example.commonutils.config.security;

import com.example.commonutils.entity.R;
import com.example.commonutils.entity.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当用户请求了一个受保护的资源，但是用户没有通过认证，那么抛出异常，AuthenticationEntryPoint.Commence(..)
 * 就会被调用，这个对应的代码在Exception TranslationFilter中，当Exception TranslationFilter到异常后，就
 * 会间接调用AuthenticationEntryPoint
 */
@Component
public class RestAuthorizationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        R outLogin = R.error(ResultCode.LOGINERROR);
        out.write(new ObjectMapper().writeValueAsString(outLogin));
        out.flush();
        out.close();
    }
}
