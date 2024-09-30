package com.seproject.buildmanager.controller;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.common.ExcelFileService;
import com.seproject.buildmanager.common.MstCodeEnums;
import com.seproject.buildmanager.config.TransactionTokenCheck;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.entity.MstSupplierManagement;
import com.seproject.buildmanager.form.MstSupplierManagementForm;
import com.seproject.buildmanager.repository.MstSupplierManagementRepository;
import com.seproject.buildmanager.service.MstCodeService;
import com.seproject.buildmanager.service.MstSupplierManagementService;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("supplier-management")
public class SupplierManagementController {
  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstCodeService mstCodeService;

  private final MstSupplierManagementRepository mstSupplierRepository;

  private final MstSupplierManagementService mstSupplierService;


  // Enumから都道府県のcode_kindの値を取得
  private static final int PREFECTURES = MstCodeEnums.PREFECTURES.getValue();

  // 同じく業者・個人のcode_kindの値を取得
  private static final int SELECT_SUPPLIER = MstCodeEnums.SELECT_SUPPLIER.getValue();

  // 引数付きコンストラクタ
  public SupplierManagementController(MstCodeService mstCodeService,
      MstSupplierManagementService mstSupplierService,
      MstSupplierManagementRepository mstSupplierRepository) {
    this.mstSupplierRepository = mstSupplierRepository;
    this.mstSupplierService = mstSupplierService;
    this.mstCodeService = mstCodeService;
  }

  @GetMapping("")
  public String getAllSupplierManagement(HttpServletRequest request, Model model) {
    logger.info("--- OwnerController.getAllOwner START ---");
    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("supplier", mstSupplierService.viewSupplierForm());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("searchSupplier", mstSupplierService.searchForm());

    // 都道府県のリストを取得してHTMLに渡す
    List<MstCode> prefecture = mstCodeService.getCodeByKind(PREFECTURES);
    model.addAttribute("prefectures", prefecture);

    logger.info("--- UserController.getAllUsers END ---");
    return "supplierManagement/supplier";

  }

  // ---------------------------新規登録---------------------------------
  @GetMapping("register")
  public String registerSupplierForm(HttpSession session, Model model) {// 新規登録画面

    logger.info("--- MstSupplierManagementController.registerSupplierForm START ---");

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    // 都道府県のリスト
    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
    // 業者か個人か
    model.addAttribute("select_supplier", mstCodeService.getCodeByKind(SELECT_SUPPLIER));

    model.addAttribute("mstSupplierManagement", mstSupplierService.showSupplierManagementForm());

    logger.info("--- OwnerController.registerOwnerForm END ---");

    return "supplierManagement/supplier_register";
  }

  // @Validated(ValidationGroups.Registration.class)でバリデーションのチェック対象を指定
  // バリデーションの対象になるクラスには@ModelAttribute("名前")による指定も行う
  // (@ModelAttributeで指定した値は自動的にModelに格納されるため、値を保持したまま、次の画面に遷移できる)
  // BindingResult bindingResultにチェックの結果が入る。
  @PostMapping("register")
  public String processRegistration(
      @ModelAttribute("mstSupplierManagement") @Validated(ValidationGroups.Registration.class) MstSupplierManagementForm mstSupplierForm,
      BindingResult bindingResult, HttpSession session, Model model) {


    logger.info("--- CustomerController.processRegistration START ---");
    if (bindingResult.hasErrors()) {
      List<MstSupplierManagement> supplier = mstSupplierService.getAllSupplierManagement();
      model.addAttribute("supplier", supplier);
      String transactionToken = UUID.randomUUID().toString();
      session.setAttribute("transactionToken", transactionToken);
      model.addAttribute("transactionToken", transactionToken);


      // 都道府県のリスト
      model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
      // 業者か個人か
      model.addAttribute("select_supplier", mstCodeService.getCodeByKind(SELECT_SUPPLIER));

      model.addAttribute("transactionToken", transactionToken);
      return "supplierManagement/supplier_register";
    }

    model.addAttribute("mstSupplierForm", mstSupplierForm);
    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);



    logger.info("--- CustomerController.processRegistration END ---");
    return "supplierManagement/supplier_register_confirm";
  }

  // 保存
  @PostMapping("save")
  @TransactionTokenCheck("save")
  public String saveSupplier(
      @ModelAttribute("mstSupplierForm") MstSupplierManagementForm mstSupplierForm) {

    logger.info("--- SupplierManagementController.savesaveSupplierManagement START ---");
    mstSupplierForm.setId(null);
    mstSupplierService.saveSupplierManagement(mstSupplierForm);

    logger.info("--- SupplierManagementController.savesaveSupplierManagement END ---");
    return "redirect:/supplier-management/save";
  }

  @GetMapping("save")
  public String createComplete() {
    return "supplierManagement/supplier_register_end";
  }

  // 変更
  @GetMapping("update")
  public String updateSupplierForm(@RequestParam(value = "id") int id, HttpSession session,
      Model model) {
    logger.info("--- CustomerController.updateCustomerForm START ---");

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
    // 業者か個人か
    model.addAttribute("select_supplier", mstCodeService.getCodeByKind(SELECT_SUPPLIER));

    MstSupplierManagement mstSupplier = mstSupplierService.getSuppliereById(id);
    model.addAttribute("mstSupplier", mstSupplierService.updateSupplierForm(mstSupplier));

    // model.addAttribute("mstCustomerForm", mstCustomerService.showCustomerForm());

    logger.info("--- CustomerController.updateCustomerForm END ---");

    return "supplierManagement/supplier_update";
  }

  @PostMapping("update")
  public String processUpdate(
      @ModelAttribute("mstSupplier") @Validated(ValidationGroups.Registration.class) MstSupplierManagementForm mstSupplierForm,
      BindingResult result, HttpSession session, Model model) {
    logger.info("--- CustomerController.processUpdate START ---");

    if (result.hasErrors()) {


      model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
      // 業者か個人か
      model.addAttribute("select_supplier", mstCodeService.getCodeByKind(SELECT_SUPPLIER));


      String transactionToken = (String) session.getAttribute("transactionToken");


      model.addAttribute("transactionToken", transactionToken);
      return "supplierManagement/supplier_update";
    }
    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);
    model.addAttribute("mstSupplierForm", mstSupplierForm);

    logger.info("--- CustomerController.processUpdate END ---");
    return "supplierManagement/supplier_update_confirm";
  }

  // 更新保存
  @PostMapping("saveUpdate")
  @TransactionTokenCheck("saveUpdate")
  public String saveSupplierUpdate(
      @ModelAttribute("mstSupplierForm") MstSupplierManagementForm mstSupplierForm) {// DBに登録

    logger.info("--- CustomerController.saveCustomer START ---");

    mstSupplierService.saveSupplierManagement(mstSupplierForm);

    logger.info("--- CustomerController.saveCustomer END ---");
    return "redirect:/supplier-management/saveUpdate";
  }

  @GetMapping("saveUpdate")
  public String createCompleteUpdate() {// 変更完了画面
    return "supplierManagement/supplier_update_end";
  }

  @PostMapping("poi")
  public String generateExcel(
      @ModelAttribute("searchSupplier") MstSupplierManagementForm mstSupplierManagementForm,
      HttpServletResponse response) {
    mstSupplierService.download(mstSupplierService
        .changeSupplierForm(mstSupplierService.search(mstSupplierManagementForm)));
    return "redirect:/supplier-management";

  }

  @PostMapping("poi2")
  public String upload() {
    mstSupplierService.upload();
    return "redirect:/supplier-management";
  };


  @PostMapping("upload")
  public String uploadFile() {
    try {
      ExcelFileService<MstSupplierManagement> fileService =
          new ExcelFileService<MstSupplierManagement>("業者仕入れ管理データ.xlsx");

      fileService.setSheetName("sheet1");
      int rowNum = fileService.getRowNum();
      for (int i = 1; i < rowNum + 1; i++) {
        // ownerList.add(fileService.importDataToRowTypeExcel(new MstOwner(), "sheet1", i));
        mstSupplierRepository
            .save(fileService.importDataToRowTypeExcel(new MstSupplierManagement(), "sheet1", i));
      }
      return "redirect:/supplier-management";
    } catch (Exception e) {
      return "redirect:/supplier-management";
    }
  }

  @GetMapping("search")
  public String search(HttpServletRequest request, Model model,
      @ModelAttribute("searchSupplierForm") MstSupplierManagementForm mstSupplierManagementForm) {
    model.addAttribute("searchSupplier", mstSupplierService.showSupplierManagementForm());

    // 都道府県のリスト
    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
    // 業者か個人か
    model.addAttribute("select_supplier", mstCodeService.getCodeByKind(SELECT_SUPPLIER));

    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("supplier", mstSupplierService.search(mstSupplierManagementForm));
    return "supplierManagement/supplier";
  }

}
