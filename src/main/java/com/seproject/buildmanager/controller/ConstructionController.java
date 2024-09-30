package com.seproject.buildmanager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.form.MstConstructionForm;
import com.seproject.buildmanager.service.MstConstructionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("construction")
public class ConstructionController {
  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstConstructionService mstConstructionService;

  // コンストラクタインジェクションを使用してMstCheckGroupeServiceの依存性を注入
  public ConstructionController(MstConstructionService mstConstructionService) {
    super();
    this.mstConstructionService = mstConstructionService;
  }

  @GetMapping("") // 工事区分分類管理画面を表示する
  public String viewconstruction(Model model, HttpServletRequest request) {
    logger.info("--- ConstructionController.viewconstruction START ---");

    logger.info("--- ConstructionController.viewConstruction END ---");

    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("MstConstructionForm", mstConstructionService.mstConstructionForm());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("constructions", mstConstructionService.getAllConstructions());
    model.addAttribute("searchConstruction", mstConstructionService.mstConstructionForm());

    // 検索
    model.addAttribute("search", mstConstructionService.mstConstructionForm());
    return "construction/construction";
  }

  @PostMapping("poi")
  public String generateExcel(
      @ModelAttribute("searchConstruction") MstConstructionForm mstConstructionForm,
      HttpServletResponse response) {
    mstConstructionService.download(mstConstructionService.search(mstConstructionForm));
    return "redirect:/construction";
  }

  @PostMapping("poi2")
  public String upload() {
    mstConstructionService.upload();
    return "redirect:/construction";
  }

  @GetMapping("search")
  public String search(HttpServletRequest request, Model model,
      @ModelAttribute("search") MstConstructionForm mstConstructionForm) {
    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("MstConstructionForm", mstConstructionService.mstConstructionForm());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("constructions", mstConstructionService.search(mstConstructionForm));
    model.addAttribute("searchConstruction", mstConstructionForm);

    // 検索
    model.addAttribute("search", mstConstructionService.mstConstructionForm());
    return "construction/construction";

  }

}
