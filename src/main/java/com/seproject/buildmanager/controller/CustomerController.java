package com.seproject.buildmanager.controller;

import java.io.IOException;
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
import com.seproject.buildmanager.common.MstCodeEnums;
import com.seproject.buildmanager.config.TransactionTokenCheck;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.form.MstCustomerForm;
import com.seproject.buildmanager.service.MstCodeService;
import com.seproject.buildmanager.service.MstCustomerService;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("customer")
public class CustomerController {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstCustomerService mstCustomerService;

  private final MstCodeService mstCodeService;

  // Enumから都道府県のcode_kindの値を取得
  private static final int PREFECTURES = MstCodeEnums.PREFECTURES.getValue();

  // 同じく法人・個人のcode_kindの値を取得
  private static final int INDIVIDUAL_CORPORATE = MstCodeEnums.INDIVIDUAL_CORPORATE.getValue();

  // 引数付きコンストラクタ
  public CustomerController(MstCustomerService mstCustomerService, MstCodeService mstCodeService) {
    super();
    this.mstCustomerService = mstCustomerService;
    this.mstCodeService = mstCodeService;
  }


  @GetMapping("")
  public String getAllCustomer(HttpServletRequest request, Model model) {

    logger.info("--- CustomerController.getAllCustomer START ---");

    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("customer", mstCustomerService.viewCustomerForm());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("mstCustomer", mstCustomerService.showCustomerForm());
    model.addAttribute("searchCustomer", mstCustomerService.showCustomerForm());


    // 都道府県のリストを取得してHTMLに渡す
    List<MstCode> prefecture = mstCodeService.getCodeByKind(PREFECTURES);
    model.addAttribute("prefectures", prefecture);

    logger.info("--- CustomerController.getAllcustomer END ---");
    return "customer/kokyaku";
  }

  // 登録
  @GetMapping("register")
  public String showCustomerForm(HttpSession session, Model model) {

    logger.info("--- CustomerController.showCustomerForm START ---");

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    model.addAttribute("mstCustomer", mstCustomerService.showCustomerForm());

    // 都道府県のリスト
    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
    // 法人か個人か
    model.addAttribute("individual", mstCodeService.getCodeByKind(INDIVIDUAL_CORPORATE));

    logger.info("--- CustomerController.showCustomerForm END ---");

    return "customer/kokyaku_register";
  }


  @PostMapping("register")
  public String processRegistration(
      @ModelAttribute("mstCustomer") @Validated(ValidationGroups.Registration.class) MstCustomerForm mstCustomerForm,
      BindingResult bindingResult, HttpSession session, Model model) {


    logger.info("--- CustomerController.processRegistration START ---");
    if (bindingResult.hasErrors()) {
      List<MstCustomer> customer = mstCustomerService.getAllCustomers();
      model.addAttribute("customer", customer);
      String transactionToken = UUID.randomUUID().toString();
      session.setAttribute("transactionToken", transactionToken);
      model.addAttribute("transactionToken", transactionToken);


      // 都道府県のリスト
      model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
      // 法人か個人か
      model.addAttribute("individual", mstCodeService.getCodeByKind(INDIVIDUAL_CORPORATE));

      return "customer/kokyaku_register";
    }

    model.addAttribute("mstCustomerForm", mstCustomerForm);



    logger.info("--- CustomerController.processRegistration END ---");
    return "customer/kokyaku_register_confirm";
  }

  // 保存
  @PostMapping("save")
  @TransactionTokenCheck("save")
  public String saveCustomer(@ModelAttribute("mstCustomerForm") MstCustomerForm mstCustomerForm) {

    logger.info("--- CustomerController.saveCustomer START ---");
    // mstCustomerForm.setId(null);
    mstCustomerService.saveCustomer(mstCustomerForm);

    logger.info("--- CustomerController.saveCustomer END ---");
    return "redirect:/customer/save";
  }

  @GetMapping("save")
  public String createComplete() {
    return "customer/kokyaku_register_end";
  }


  // 変更
  @GetMapping("update")
  public String updateCustomerForm(@RequestParam(value = "id") int id, HttpSession session,
      Model model) {
    logger.info("--- CustomerController.updateCustomerForm START ---");

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
    model.addAttribute("individual", mstCodeService.getCodeByKind(INDIVIDUAL_CORPORATE));

    MstCustomer mstCustomer = mstCustomerService.getCustomerById(id);
    model.addAttribute("mstCustomer", mstCustomerService.updateCustomerForm(mstCustomer));

    // model.addAttribute("mstCustomerForm", mstCustomerService.showCustomerForm());

    logger.info("--- CustomerController.updateCustomerForm END ---");

    return "customer/kokyaku_update";
  }

  @PostMapping("update")
  public String processUpdate(
      @ModelAttribute("mstCustomer") @Validated(ValidationGroups.Registration.class) MstCustomerForm mstCustomerForm,
      BindingResult result, HttpSession session, Model model) {
    logger.info("--- CustomerController.processUpdate START ---");

    if (result.hasErrors()) {


      model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
      model.addAttribute("individual", mstCodeService.getCodeByKind(INDIVIDUAL_CORPORATE));


      String transactionToken = (String) session.getAttribute("transactionToken");


      model.addAttribute("transactionToken", transactionToken);
      return "customer/kokyaku_update";
    }

    model.addAttribute("mstCustomerForm", mstCustomerForm);

    logger.info("--- CustomerController.processUpdate END ---");
    return "customer/kokyaku_update_confirm";
  }

  // 更新保存
  @PostMapping("save-update")
  @TransactionTokenCheck("saveUpdate")
  public String saveCustomerUpdate(
      @ModelAttribute("mstCustomerForm") MstCustomerForm mstCustomerForm) {// DBに登録

    logger.info("--- CustomerController.saveCustomer START ---");

    mstCustomerService.saveCustomer(mstCustomerForm);

    logger.info("--- CustomerController.saveCustomer END ---");
    return "redirect:/customer/save-update";
  }

  @GetMapping("save-update")
  public String createCompleteUpdate() {// 変更完了画面
    return "customer/kokyaku_update_end";
  }

  @PostMapping("download")
  public String generateExcel(@ModelAttribute("searchCustomer") MstCustomerForm mstCustomerForm)
      throws IOException {
    // ExcelFileService<MstCustomer> fileService = new ExcelFileService<MstCustomer>("顧客データ.xlsx");
    List<MstCustomer> customer =
        mstCustomerService.changeCustomerForm(mstCustomerService.search(mstCustomerForm));
    mstCustomerService.download(customer);
    return "redirect:/customer";
  }

  @PostMapping("upload")
  public String uploadFile() {
    mstCustomerService.upload();
    return "redirect:/customer";
  }

  @GetMapping("search")
  public String search(HttpServletRequest request, Model model,
      @ModelAttribute("mstCustomerForm") MstCustomerForm mstCustomerForm) {
    model.addAttribute("mstCustomer", mstCustomerService.showCustomerForm());
    model.addAttribute("searchCustomer", mstCustomerService.showCustomerForm());
    // 都道府県のリスト
    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
    // 法人か個人か
    model.addAttribute("cust_kind", mstCodeService.getCodeByKind(INDIVIDUAL_CORPORATE));
    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("customer", mstCustomerService.search(mstCustomerForm));
    return "customer/kokyaku";
  }

}

