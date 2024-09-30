package com.seproject.buildmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.form.MstAuthTypeForm;
import com.seproject.buildmanager.service.MstAuthTypeService;


@Controller
@RequestMapping("AuthType")
public class AuthTypeController {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  @Autowired
  private MstAuthTypeService mstAuthTypeService;

  // コンストラクタインジェクションを使用してMstAuthTypeServiceの依存性を注入
  public AuthTypeController(MstAuthTypeService mstAuthTypeService) {
    this.mstAuthTypeService = mstAuthTypeService;
  }

  @GetMapping("")
  public String getAllAuthType(Model model, HttpServletRequest request) {


    // logger.info("--- UserController.getAllUsers START ---");

    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("mstAuthTypeForm", mstAuthTypeService.mstAuthTypeForm());
    model.addAttribute("constructions", mstAuthTypeService.getAllAuthType());
    model.addAttribute("AuthType", mstAuthTypeService.getAllAuthType());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("authtypeForm", mstAuthTypeService.mstAuthTypeForm());
    // logger.info("--- UserController.getAllUsers END ---");
    return "authType/authType";
  }

  @GetMapping("search")
  public String search(HttpServletRequest request, Model model,
      @ModelAttribute("authtypeForm") MstAuthTypeForm mstAuthTypeForm) {
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("mstAuthTypeForm", mstAuthTypeService.mstAuthTypeForm());
    model.addAttribute("constructions", mstAuthTypeService.search(mstAuthTypeForm));
    model.addAttribute("AuthType", mstAuthTypeService.search(mstAuthTypeForm));
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("authtypeForm", mstAuthTypeService.mstAuthTypeForm());
    return "authType/authType";
  }
  // @PostMapping("save") // フォームに入力されたグループ名を受け取ってsarviceメソッドを実行して、同じ画面へ遷移する
  // public String saveCheck_group(@RequestParam("check_save") String check, Model model) {
  //
  // mstCheckService.saveCheck(check);
  // return "redirect:/check";
  // }

}
