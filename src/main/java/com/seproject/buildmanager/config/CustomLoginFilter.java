package com.seproject.buildmanager.config;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 認証を行う前後に処理をするためにオリジナルのfilterを作成
// ここでusernameをCookieに登録
public class CustomLoginFilter implements Filter {

  // doFilterに具体的な処理を書く
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;


    String tenantId = request.getParameter("remember");
    if (tenantId != null) {
      String username = request.getParameter("username");
      // Cookieを作成し、userNameを保存
      Cookie cookie = new Cookie("cookieUserName", username);
      cookie.setMaxAge(60 * 60 * 24);
      response.addCookie(cookie);
    }

    filterChain.doFilter(request, response); // 各Filterの処理を順に実行
  }
}
