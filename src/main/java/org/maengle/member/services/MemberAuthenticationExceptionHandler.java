package org.maengle.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;

public class MemberAuthenticationExceptionHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String URL = request.getRequestURI();
        System.out.println(URL);
        String qs = request.getQueryString();
        URL = StringUtils.hasText(qs) ? URL + "?" + URLEncoder.encode(qs, "UTF-8") : URL;
        String a = request.getContextPath();
        System.out.println(a);

        // 로그인 페이지를 모달창으로 설정할 예정이기 때문에 메인페이지로 이동되도록 설정
        response.sendRedirect(String.format("%s/member/login?redirectUrl=%s",request.getContextPath(), URL));
    }
}
