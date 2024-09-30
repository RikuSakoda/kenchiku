package com.seproject.buildmanager.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.service.MstFacilitiesTitleService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("facilities-item")
public class FacilitiesItemController {

  private final MstFacilitiesTitleService mstFacilitiesTitleService;

  public FacilitiesItemController(MstFacilitiesTitleService mstFacilitiesTitleService) {
    this.mstFacilitiesTitleService = mstFacilitiesTitleService;
  }

  @GetMapping("")
  public String facilitiesList(HttpServletRequest request, Model model) {
    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());

    model.addAttribute("mstFacilities", this.mstFacilitiesTitleService.getFacilitiesTitles());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);

    return "facilitiesList/list.html";
  }
}
