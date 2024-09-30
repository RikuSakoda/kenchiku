package com.seproject.buildmanager.controller;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.form.MstCheckChangeForm;
import com.seproject.buildmanager.service.MstCheckChangeService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("check-change")
public class CheckChangeController {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  @Autowired
  private MstCheckChangeService mstCheckChangeService; // サービスのインスタンス

  public CheckChangeController(MstCheckChangeService mstCheckChangeService) {
    super();
    this.mstCheckChangeService = mstCheckChangeService;
  }

  @GetMapping("")
  public String getCheckGroup(Model model, HttpServletRequest request) {

    logger.info("--- CheckChangeController.getCheckGroup START ---");

    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());

    model.addAttribute("checkchange", this.mstCheckChangeService.getAllGroupes());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);

    model.addAttribute("MstCheckGroupeForm", this.mstCheckChangeService.mstCheckChangeForm());
    model.addAttribute("mstCheckGroup", this.mstCheckChangeService.mstCheckChangeForm());
    model.addAttribute("searchCheckChange", mstCheckChangeService.mstCheckChangeForm());
    // DBデータ全表示呼び出しメソッドをオブジェクトへ追加
    logger.info("--- CheckChangeController.getCheckGroup END ---");

    return "check/check_group_register";// 同じ画面へ返す
  }

  @GetMapping("search")
  public String search(HttpServletRequest request, Model model,
      @ModelAttribute("mstCheckGroup") MstCheckChangeForm mstCheckGroupForm) {
    model.addAttribute("mstCheckGroup", mstCheckChangeService.mstCheckChangeForm());

    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    // model.addAttribute("checkGroup", mstCheckChangeService.search(mstCheckGroupForm));
    model.addAttribute("MstCheckGroupeForm", this.mstCheckChangeService.mstCheckChangeForm());
    model.addAttribute("checkchange", mstCheckChangeService.search(mstCheckGroupForm));
    model.addAttribute("searchCheckChange", mstCheckGroupForm);
    return "check/check_group_register";
  }

  @PostMapping("download")
  public String generateExcel(
      @ModelAttribute("searchCheckChange") MstCheckChangeForm mstCheckChangeForm)
      throws IOException {
    mstCheckChangeService.download(this.mstCheckChangeService.search(mstCheckChangeForm));
    return "redirect:/check-change";
  }

  @PostMapping("poi2")
  public String upload() {
    mstCheckChangeService.upload();
    return "redirect:/check-change";
  }


}
