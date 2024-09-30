package com.seproject.buildmanager.controller;

import java.io.IOException;
import java.util.List;
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
import com.seproject.buildmanager.common.ExcelFileService;
import com.seproject.buildmanager.entity.MstMaterialsManagement;
import com.seproject.buildmanager.form.MstMaterialsManagementForm;
import com.seproject.buildmanager.service.MstMaterialsManagementService;
import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("materials")
public class MaterialsManagementController {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  @Autowired
  private MstMaterialsManagementService mstMaterialsService;

  private final ExcelFileService<MstMaterialsManagement> fileService;

  // コンストラクタインジェクションを使用してMstCheckGroupeServiceの依存性を注入
  public MaterialsManagementController(MstMaterialsManagementService mstMaterialsService,
      ExcelFileService<MstMaterialsManagement> fileService) {
    super();
    this.mstMaterialsService = mstMaterialsService;
    this.fileService = fileService;
    this.fileService.initializeExcel("資材メーカー管理.xlsx");
  }

  @GetMapping("")
  public String getAllMaterials(Model model, HttpServletRequest request) {


    // logger.info("--- UserController.getAllUsers START ---");

    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("MstMaterialsForm", mstMaterialsService.mstMaterialsForm());
    model.addAttribute("searchMaterials", mstMaterialsService.mstMaterialsForm());
    model.addAttribute("constructions", mstMaterialsService.getAllMaterials());
    model.addAttribute("materials", mstMaterialsService.getAllMaterials());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("materialsForm", mstMaterialsService.mstMaterialsForm());
    // logger.info("--- UserController.getAllUsers END ---");
    return "materials/Materials";
  }

  @GetMapping("search")
  public String search(HttpServletRequest request, Model model,
      @ModelAttribute("materialsForm") MstMaterialsManagementForm mstMaterialsForm) {
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("MstMaterialsForm", mstMaterialsService.mstMaterialsForm());
    model.addAttribute("searchMaterials", mstMaterialsService.mstMaterialsForm());
    model.addAttribute("constructions", mstMaterialsService.getAllMaterials());
    model.addAttribute("materials", mstMaterialsService.search(mstMaterialsForm));
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("materialsForm", mstMaterialsService.mstMaterialsForm());
    return "materials/Materials";
  }

  @PostMapping("poi")
  public String generateExcel(@ModelAttribute("searchMaterials") MstMaterialsManagementForm mstMaterialsForm)
      throws IOException {
    List<MstMaterialsManagement> materials =
        mstMaterialsService.changeMaterialsForm(mstMaterialsService.search(mstMaterialsForm));
    mstMaterialsService.download(materials);
    return "redirect:/materials";
  }

  @PostMapping("poi2")
  public String upload() {
    mstMaterialsService.upload();
    return "redirect:/materials";
  }
}
