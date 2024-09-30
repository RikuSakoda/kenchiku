package com.seproject.buildmanager.controller;

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
import com.seproject.buildmanager.form.MstFloorNameForm;
import com.seproject.buildmanager.service.FloorNameService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("floor-name")
public class FloorNameController {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  @Autowired
  private FloorNameService floorNameService;



  @GetMapping("")
  public String getCheck_group(Model model, HttpServletRequest request) {

    logger.info("--- FloorNameController.getAllFloor START ---");
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("FloorNameInfo", floorNameService.viewFloorNameForm());
    // model.addAttribute("FloorName", floorNameService.getAllFloor());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("mstFloorName", floorNameService.mstFloorNameForm());
    // model.addAttribute("MstFloorNameForm", floorNameService.mstFloorNameForm());

    // DBデータ全表示呼び出しメソッドをオブジェクトへ追加
    logger.info("--- FloorNameController.getAllFloor END ---");
    return "floor/floor_plan_name_main";// 同じ画面へ返す
  }

  @GetMapping("search")
  public String search(HttpServletRequest request, Model model,
      @ModelAttribute("mstFloorName") MstFloorNameForm mstFloorNameForm) {
    model.addAttribute("mstFloorName", floorNameService.mstFloorNameForm());
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("FloorNameInfo", floorNameService.search(mstFloorNameForm));
    return "floor/floor_plan_name_main";

  }
}
