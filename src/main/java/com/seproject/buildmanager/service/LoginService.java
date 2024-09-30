package com.seproject.buildmanager.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;

@Service
public class LoginService {

  private final EmailService emailServise;

  private LoginService(EmailService emailServise) {
    this.emailServise = emailServise;
  }

  public void SendResetPasswordMail(String email, String emailToken, String loginCd) {
    Map<String, Object> variables = new HashMap<>();
    variables.put("name", loginCd);
    variables.put("loginId", loginCd);
    variables.put("emailToken", emailToken);
    try {
      emailServise.sendHtmlEmail(email, "パスワードリセット", variables, "/sample.txt", null);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
