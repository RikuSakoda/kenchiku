package com.seproject.buildmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.seproject.buildmanager.form.MstFacilitiesManagementForm;
import com.seproject.buildmanager.service.MstFacilitiesService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("facilities")
public class FacilitiesController {

  @Autowired
  private MstFacilitiesService mstFacilitiesService;

  public FacilitiesController() {

  }

  @GetMapping("")
  public String getAllFacilities(HttpServletRequest request, Model model) {
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    // model.addAttribute("facilitiesManagements",
    // mstFacilitiesService.viewFacilitiesManagementForm());


    return "facilities/facilities";
  }

  @GetMapping("List")
  public String getListFacilities(@RequestParam(value = "id") Integer id,
      HttpServletRequest request, Model model) {
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("facilitiesManagementForm",
        this.mstFacilitiesService.getFacilitieManagementByMatterIdFromForm(id));

    return "facilities/facilitiesList";
  }

  @PostMapping("List") // modelattributeの中身変更
  public String saveListFacilities(
      @ModelAttribute("facilitiesManagementForm") MstFacilitiesManagementForm managementForm,
      HttpServletRequest request, Model model) {
    // save処理

    this.mstFacilitiesService.save(managementForm);
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    return "facilities/facilitiesList";
  }
}
