package com.seproject.buildmanager.config;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TransactionTokenInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (handler instanceof HandlerMethod) {
      HandlerMethod handlerMethod = (HandlerMethod) handler;
      TransactionTokenCheck tokenCheck =
          handlerMethod.getMethodAnnotation(TransactionTokenCheck.class);

      if (tokenCheck != null) {
        String token = request.getParameter("transactionToken");
        String sessionToken = (String) request.getSession().getAttribute("transactionToken");

        if (token == null || !token.equals(sessionToken)) {
          response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid transaction token");
          return false;
        }

        request.getSession().removeAttribute("transactionToken"); // One-time token usage
      }
    }
    return true;
  }
}
