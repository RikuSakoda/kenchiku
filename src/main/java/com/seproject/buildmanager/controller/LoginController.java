package com.seproject.buildmanager.controller;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.seproject.buildmanager.config.TransactionTokenCheck;
import com.seproject.buildmanager.entity.MstUser;
import com.seproject.buildmanager.form.ResetPasswordForm;
import com.seproject.buildmanager.repository.MstUserRepository;
import com.seproject.buildmanager.service.LoginService;
import com.seproject.buildmanager.service.MstUserService;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.servlet.http.HttpSession;

/**
 * ログイン画面を提供するコントローラクラスです。
 * 
 * <p>
 * このクラスは、ログイン画面へのリクエストを処理し、ログインページを表示します。
 * 
 * <p>
 * 変更履歴：
 * <ul>
 * <li>2024/10/31 - 初版作成</li>
 * </ul>
 * 
 * @since 1.0
 * @version 1.0
 */
@Controller
public class LoginController {

  @Autowired
  private MstUserService mstUserService;

  @Autowired
  private MstUserRepository mstUserRepository;

  @Autowired
  private LoginService loginService;

  /**
   * ログイン画面を表示します。
   * 
   * <p>
   * このメソッドは、"/login" パスへのGETリクエストを処理し、ログインページのテンプレート名を返します。
   * 
   * @return ログインページのテンプレート名 ("login")
   */
  @GetMapping("/login")
  public String login(@CookieValue(name = "cookieUserName", required = false) String userName,
      Model model, HttpSession session) {
    // cookieがあ
    BadCredentialsException springException =
        (BadCredentialsException) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
    if (springException != null) {
      String message = springException.getMessage();
      model.addAttribute("message", message);
      session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
    } else {
      model.addAttribute("message", "");
    }
    if (userName != null) {
      model.addAttribute("loginUserName", userName);
    } else {
      model.addAttribute("loginUserName", "");
    }

    return "login";
  }

  @GetMapping("/forgotPassword")
  public String forgotPassword(Model model) {
    model.addAttribute("message", "");
    return "forgot_password";
  }

  @PostMapping("/forgotPassword")
  public String forgotPasswordEnd(@RequestParam(value = "email") String email,
      @RequestParam("loginId") String loginId, HttpSession session, Model model) {// リクエストパラムがほしいかも
    MstUser mstUser = mstUserRepository.findByLogin(loginId);
    if (mstUser == null || !mstUser.getEmail().equals(email)) {
      model.addAttribute("message", "パスワードまたは、メールアドレスが違います");
      return "forgot_password";
    }
    String emailToken = UUID.randomUUID().toString();
    session.setAttribute(loginId, emailToken);

    loginService.SendResetPasswordMail(email, emailToken, loginId);

    return "forgot_password_end";
  }

  @GetMapping("/emailRedirect")
  public String redirectEmail(@RequestParam("emailToken") String emailToken, HttpSession session,
      @RequestParam("loginId") String loginId, RedirectAttributes redirectAttributes) {
    String sessionEmail = (String) session.getAttribute(loginId);
    session.removeAttribute(loginId);
    if (sessionEmail == null || !sessionEmail.equals(emailToken)) {
      return "redirect:/errorPassword";
    }
    redirectAttributes.addFlashAttribute("loginId", loginId);
    return "redirect:/resetPassword";
  }

  @GetMapping("/errorPassword")
  public String mailError() {
    return "error_password";
  }

  @GetMapping("/resetPassword")
  public String resetPassword(HttpSession session, Model model,
      @ModelAttribute("loginId") String loginId) {
    if (loginId == null || loginId.isEmpty()) {
      return "redirect:/errorPassword";
    }
    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);
    ResetPasswordForm resetForm = mstUserService.resetPasswordForm();
    resetForm.setLoginCd(loginId);
    // model.addAttribute("mstUserForm", mstUserService.showUserForm());
    model.addAttribute("resetPasswordForm", resetForm);
    return "reset_password";
  }

  @PostMapping("/save")
  @TransactionTokenCheck("/save")
  public String savePassword(
      @ModelAttribute("resetPasswordForm") @Validated(ValidationGroups.Registration.class) ResetPasswordForm resetPasswordForm,
      BindingResult bindingResult, HttpSession session, Model model) {
    if (bindingResult.hasErrors()) {
      String transactionToken = UUID.randomUUID().toString();
      session.setAttribute("transactionToken", transactionToken);
      model.addAttribute("transactionToken", transactionToken);
      return "reset_password";
    }
    mstUserService.update(resetPasswordForm);
    return "redirect:/save";
  }

  @GetMapping("/save")
  public String resetPasswordEnd() {
    return "reset_password_end";
  }
}
