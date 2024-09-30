package com.seproject.buildmanager.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.seproject.buildmanager.config.LoginUserDetails;
import com.seproject.buildmanager.config.TransactionTokenCheck;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.entity.MstConstruction;
import com.seproject.buildmanager.entity.MstConstructionClassificationManagement;
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.form.MstConstructionClassificationManagementtForm;
import com.seproject.buildmanager.service.MstCodeService;
import com.seproject.buildmanager.service.MstConstructionClassificationManagementService;
import com.seproject.buildmanager.service.MstConstructionService;
import com.seproject.buildmanager.service.MstCustomerService;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("cost")
public class ConstructionClassificationManagementController {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  @Autowired
  private MstConstructionClassificationManagementService mstCostService;

  @Autowired
  private MstCustomerService mstCustomerService;

  @Autowired
  private MstConstructionService mstConstructionService;

  @Autowired
  private MstCodeService mstCodeService;

  // Enumから単価のunit_idの値を取得
  private static final int UNIT = MstCodeEnums.UNIT.getValue();
  //
  // // 同じく法人・個人のcode_kindの値を取得
  // private static final int INDIVIDUAL_CORPORATE = MstCodeEnums.INDIVIDUAL_CORPORATE.getValue();


  @GetMapping("")
  public String getAllCost(HttpServletRequest request, Model model) {

    logger.info("--- CostController.getAllCost START ---");

    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("cost", mstCostService.getAllCost());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("mstCost", mstCostService.registerCostForm());
    model.addAttribute("searchCost", mstCostService.registerCostForm());

    // 単位のリストを取得してHTMLに渡す
    List<MstCode> unitId = mstCodeService.getCodeByKind(UNIT);
    model.addAttribute("unitId", unitId);

    logger.info("--- CostController.getAllCost END ---");
    return "constructionClassificationManagement/cost";
  }


  // // 登録
  @GetMapping("register")
  public String registerCostForm(HttpSession session, Model model) {// 新規登録画面

    logger.info("--- CostController.registerCostForm START ---");

    List<MstCustomer> customer = mstCustomerService.getAllCustomers();
    model.addAttribute("customer", customer);
    model.addAttribute("inputCustomer", new MstCustomer());

    List<MstConstruction> construction = mstConstructionService.getAllConstructions();
    model.addAttribute("construction", construction);
    model.addAttribute("inputConstruction", new MstConstruction());


    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    // 単位のリスト
    model.addAttribute("unitId", mstCodeService.getCodeByKind(UNIT));
    // // 法人か個人か
    // model.addAttribute("individual", mstCodeService.getCodeByKind(INDIVIDUAL_CORPORATE));

    model.addAttribute("mstCost", mstCostService.registerCostForm());

    logger.info("--- CostController.registerCostForm END ---");

    return "constructionClassificationManagement/cost_register";
  }


  @PostMapping("register")
  public String processRegistration(
      @ModelAttribute("mstCost") @Validated(ValidationGroups.Registration.class) MstConstructionClassificationManagementtForm mstCostForm,
      BindingResult bindingResult, HttpSession session, Model model) {// 新規登録確認画面

    logger.info("--- CostController.processRegistration START ---");
    // bindingResult.hasErrors()でチェックの結果を取得 trueの場合、エラーがあるということになる
    if (bindingResult.hasErrors()) {
      // Getでmodelに値をセットしている場合は、ここでセットしなおす。
      // ただし、入力用のFormクラスは、値をセットしない(ここでセットしなおすと、Formに入っていたエラーメッセージなどの情報も上書きされてしまうから)
      List<MstCustomer> customer = mstCustomerService.getAllCustomers();
      model.addAttribute("customer", customer);
      model.addAttribute("inputCustomer", new MstCustomer());

      List<MstConstruction> construction = mstConstructionService.getAllConstructions();
      model.addAttribute("construction", construction);
      model.addAttribute("inputConstruction", new MstConstruction());


      String transactionToken = UUID.randomUUID().toString();
      session.setAttribute("transactionToken", transactionToken);
      model.addAttribute("transactionToken", transactionToken);

      // 単位のリスト
      model.addAttribute("unitId", mstCodeService.getCodeByKind(UNIT));
      // // 法人か個人か
      // model.addAttribute("individual", mstCodeService.getCodeByKind(INDIVIDUAL_CORPORATE));
      return "constructionClassificationManagement/cost_register";
    }

    mstCostForm.setCustName(mstCustomerService
        .getCustomerById(Integer.parseInt(mstCostForm.getCustId())).getCorpName());
    model.addAttribute("mstCostForm", mstCostForm);

    mstCostForm.setGroupName(mstConstructionService
        .getConstructionById(Integer.parseInt(mstCostForm.getCostGroupId())).getCostGroupName());
    model.addAttribute("mstCostForm", mstCostForm);



    logger.info("--- OwnerController.processRegistration END ---");
    return "constructionClassificationManagement/cost_register_confirm";
  }

  // 保存
  @PostMapping("saveRegister")
  @TransactionTokenCheck("saveRegister")
  public String saveCostRegister(MstConstructionClassificationManagementtForm mstCostForm,
      @AuthenticationPrincipal LoginUserDetails user) {// DBに登録

    logger.info("--- CostController.saveCost START ---");


    mstCostService.saveCostRegister(mstCostForm);

    logger.info("--- CostController.saveCost END ---");
    return "redirect:/cost/saveRegister";
  }

  @GetMapping("saveRegister")
  public String createCompleteRegister() {// 新規登録完了画面
    return "constructionClassificationManagement/cost_register_end";
  }


  // 変更
  @GetMapping("update")
  public String updateCostForm(@RequestParam(value = "id") String id, HttpSession session,
      Model model) {
    logger.info("--- CostController.updateCustomerForm START ---");

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    model.addAttribute("unitId", mstCodeService.getCodeByKind(UNIT));

    MstConstructionClassificationManagement mstCost =
        mstCostService.getCostId(Integer.parseInt(id));
    model.addAttribute("mstCost", mstCostService.updateCostForm(mstCost.getId()));

    List<MstCustomer> customer = mstCustomerService.getAllCustomers();
    model.addAttribute("customer", customer);
    MstCustomer cus = mstCustomerService.getCustomerById(mstCost.getCustId());

    List<MstConstruction> construction = mstConstructionService.getAllConstructions();
    model.addAttribute("construction", construction);
    MstConstruction con = mstConstructionService.getConstructionById(mstCost.getCostGroupId());

    model.addAttribute("mstCostForm",
        mstCostService.updateCostForm(mstCost, cus.getCorpName(), con.getCostGroupName()));

    logger.info("--- CostController.updateCostForm END ---");

    return "constructionClassificationManagement/cost_update";
  }

  @PostMapping("update")
  public String processUpdate(
      @ModelAttribute("mstCostForm") @Validated(ValidationGroups.Update.class) MstConstructionClassificationManagementtForm mstCostForm,
      BindingResult bindingResult, HttpSession session, Model model) {
    logger.info("--- OwnerController.processUpdate START ---");
    if (bindingResult.hasErrors()) {
      String transactionToken = UUID.randomUUID().toString();
      session.setAttribute("transactionToken", transactionToken);
      model.addAttribute("transactionToken", transactionToken);

      model.addAttribute("unitId", mstCodeService.getCodeByKind(UNIT));


      List<MstCustomer> customer = mstCustomerService.getAllCustomers();
      model.addAttribute("customer", customer);

      List<MstConstruction> construction = mstConstructionService.getAllConstructions();
      model.addAttribute("construction", construction);


      return "constructionClassificationManagement/cost_update";
    }

    model.addAttribute("mstCostForm", mstCostService.CostForm(mstCostForm));

    logger.info("--- CostController.processUpdate END ---");
    return "constructionClassificationManagement/cost_update_confirm";
  }

  // 更新保存
  @PostMapping("saveUpdate")
  @TransactionTokenCheck("saveUpdate")
  public String saveCostUpdate(
      @ModelAttribute("mstCostForm") MstConstructionClassificationManagementtForm mstCostForm) {// DBに登録

    logger.info("--- OwnerController.saveOwner START ---");

    mstCostService.saveCostUpdate(mstCostForm);

    logger.info("--- OwnerController.saveOwner END ---");
    return "redirect:/cost/saveUpdate";
  }

  @GetMapping("saveUpdate")
  public String createCompleteUpdate() {// 変更完了画面
    return "constructionClassificationManagement/cost_update_end";
  }

  // 検索
  @GetMapping("search")
  public String search(HttpServletRequest request, Model model,
      @ModelAttribute("mstCostForm") MstConstructionClassificationManagementtForm mstCostForm) {
    model.addAttribute("mstCost", mstCostService.registerCostForm());
    model.addAttribute("searchCost", mstCostService.registerCostForm());
    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("cost", mstCostService.search(mstCostForm));
    return "constructionClassificationManagement/cost";
  }

  @PostMapping("download")
  public String generateExcel(
      @ModelAttribute("searchCost") MstConstructionClassificationManagementtForm mstCostForm)
      throws IOException {
    // ExcelFileService<MstCost> fileService = new ExcelFileService<MstCost>("工事区分データ.xlsx");
    List<MstConstructionClassificationManagement> cost =
        mstCostService.changeCostForm(mstCostService.search(mstCostForm));
    mstCostService.download(cost);
    return "redirect:/cost";
  }

  @PostMapping("upload")
  public String uploadFile() {
    mstCostService.upload();
    return "redirect:/customer";
  }
}
