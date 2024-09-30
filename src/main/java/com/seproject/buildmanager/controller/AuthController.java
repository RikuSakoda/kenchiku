package com.seproject.buildmanager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.form.MstAuthForm;
import com.seproject.buildmanager.service.MstAuthService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("auth")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  private MstAuthService mstAuthService;

  @GetMapping("")
  public String getAuth(Model model, HttpServletRequest request) {
    logger.info("--- AuthController.getAuth START ---");

    // CSRFトークンの設定
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());

    // 定数やサービスから取得したデータの設定
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("auths", mstAuthService.getAllAuth());
    model.addAttribute("authTypes", mstAuthService.getAllAuthType());
    model.addAttribute("search", mstAuthService.mstAuthForm());

    // authNameを取得してモデルに追加
    Integer authTypeId = 00; // authTypeIdの値は適切な値に修正する必要があります
    String authName = mstAuthService.getAuthNameById(authTypeId);
    model.addAttribute("authName", authName);

    logger.info("--- AuthController.getAuth END ---");
    return "auth/auth";
  }

  @GetMapping("search")
  public String search(HttpServletRequest request, Model model,
      @ModelAttribute("mstAuthTypeForm") MstAuthForm mstAuthForm) {
    // CSRFトークンの設定
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());

    // 定数やサービスから取得したデータの設定
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("auths", mstAuthService.search(mstAuthForm));
    model.addAttribute("authTypes", mstAuthService.getAllAuthType());
    model.addAttribute("search", mstAuthService.mstAuthForm());

    // authNameを取得してモデルに追加
    Integer authTypeId = 00; // authTypeIdの値は適切な値に修正する必要があります
    String authName = mstAuthService.getAuthNameById(authTypeId);
    model.addAttribute("authName", authName);
    return "auth/auth";
  }

  @PostMapping("download")
  public String generateExcel() {
    mstAuthService.download();
    return "redirect:/auth";
  }

  @PostMapping("upload")
  public String upload() {
    mstAuthService.upload();
    return "redirect:/auth";
  }
}
