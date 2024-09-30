package com.seproject.buildmanager.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.entity.MstMatter;
import com.seproject.buildmanager.entity.MstUser;
import com.seproject.buildmanager.form.MstMatterForm;
import com.seproject.buildmanager.service.CommonService;
import com.seproject.buildmanager.service.MstCodeService;
import com.seproject.buildmanager.service.MstCustomerService;
import com.seproject.buildmanager.service.MstEstimateItemService;
import com.seproject.buildmanager.service.MstEstimateManagementService;
import com.seproject.buildmanager.service.MstFloorManagementService;
import com.seproject.buildmanager.service.MstMatterService;
import com.seproject.buildmanager.service.MstUserService;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("case")
public class MatterController {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  @Autowired
  private MstMatterService mstMatterService;// サービスのインスタンス

  @Autowired
  private MstFloorManagementService mstFloorManagementService;

  @Autowired
  private CommonService commonService;

  @Autowired
  private MstEstimateItemService mstEstimateItemService;


  @Autowired
  private MstCustomerService mstCustomerService;

  @Autowired
  private MstCodeService mstCodeService;

  @Autowired
  private MstUserService mstUserService;

  @Autowired
  private MstEstimateManagementService mstEstimateManagementService;

  // Enumから都道府県のcode_kindの値を取得
  private static final int PREFECTURES = MstCodeEnums.PREFECTURES.getValue();

  // Enumから種別を取得するためののcode_kindの値を取得
  private static final int TASK_SUBSTANCE = MstCodeEnums.TASK_SUBSTANCE.getValue();

  // Enumから状況ステータスを取得するため
  private static final int SITUATION_STATUS = MstCodeEnums.SITUATION_STATUS.getValue();

  private final Integer estimatePresence = 1; // 見積もりありの状況ステータス

  private final Integer estimateAbsence = 2; // 見積なしの状況ステータス



  public MatterController(MstMatterService mstMatterService, CommonService commonService) {
    super();
    this.mstMatterService = mstMatterService;
    this.commonService = commonService;
  }

  @GetMapping("")
  public String getMatter(Model model, HttpServletRequest request) {

    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());


    DateFormat japaseseFormat = new SimpleDateFormat("GGGGy年M月d日");
    List<MstMatterForm> mML = mstMatterService.viewCaseForm();
    mML.get(0).getRegistrationDatetime();
    model.addAttribute("caseInfo", mstMatterService.viewCaseForm());
    // 検索用
    model.addAttribute("search", mstMatterService.registerCaseForm());

    model.addAttribute("bool", "true");
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    // 状況ステータスを取得するためのリスト
    model.addAttribute("situation", mstCodeService.getCodeByKind(SITUATION_STATUS));
    model.addAttribute("mstCase", mstMatterService.registerCaseForm());
    // 種別を取得するためのリスト
    model.addAttribute("caseKind", mstCodeService.getCodeByKind(TASK_SUBSTANCE));



    return "case/case";

  }

  @GetMapping("register")
  public String registerCaseForm(HttpSession session, Model model) { // 新規登録画面

    logger.info("--- CaseController.registerCaseForm START ---");

    // 顧客情報
    List<MstCustomer> customer = mstCustomerService.getAllCustomers();
    model.addAttribute("customer", customer);

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    // 都道府県のリスト
    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));

    // 種別を取得するためのリスト
    model.addAttribute("caseKind", mstCodeService.getCodeByKind(TASK_SUBSTANCE));



    model.addAttribute("mstCase", mstMatterService.registerCaseForm());

    model.addAttribute("bukken", mstFloorManagementService.viewFloorForm());

    // 訪問担当者
    List<MstUser> user = mstUserService.findAllUsersWithAuthName();
    model.addAttribute("visitUser", user);

    logger.info("--- CaseController.registerCaseForm End ---");

    return "case/case_register";

  }

  @PostMapping("register")
  public String processRegistration(
      @ModelAttribute("mstCase") @Validated(ValidationGroups.Registration.class) MstMatterForm mstMatterForm,
      BindingResult bingingResult, HttpSession session, Model model) {// 新規登録確認画面

    logger.info("--- FloorManagementController.processRegistration START ---");

    if (bingingResult.hasErrors()) {


      // 訪問担当者
      List<MstUser> user = mstUserService.findAllUsersWithAuthName();
      model.addAttribute("visitUser", user);

      // 顧客情報
      List<MstCustomer> customer = mstCustomerService.getAllCustomers();
      model.addAttribute("customer", customer);
      model.addAttribute("inputCustomer", new MstCustomer());

      model.addAttribute("bukken", mstFloorManagementService.viewFloorForm());

      String transactionToken = UUID.randomUUID().toString();
      session.setAttribute("transactionToken", transactionToken);
      model.addAttribute("transactionToken", transactionToken);
      // 都道府県のリスト
      model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));

      // 種別を取得するためのリスト
      model.addAttribute("caseKind", mstCodeService.getCodeByKind(TASK_SUBSTANCE));

      // 訪問担当者


      return "case/case_register";
    }

    // 顧客
    if (mstMatterForm.getCustomerId() != null) {
      mstMatterForm.setCustomerName(
          mstCustomerService.getCustomerById(mstMatterForm.getCustomerId()).getCorpName());


    } else {
      mstMatterForm.setCustomerName(mstCustomerService.getCustomerById(1).getCorpName());

    }

    // 訪問担当者
    if (mstMatterForm.getVisitId() != null) {

      String fName = mstUserService.getUserById(mstMatterForm.getVisitId()).getFName();
      String lName = mstUserService.getUserById(mstMatterForm.getVisitId()).getLName();

      String visitName = (fName + lName);

      mstMatterForm.setVisitName(visitName);
      model.addAttribute("mstCaseForm", mstMatterForm);
    } else {
      mstMatterForm.setVisitName(null);
      model.addAttribute("mstCaseForm", mstMatterForm);
    }



    logger.info("--- FloorManagementController.processRegistration END ---");

    return "case/case_register_confirm.html";


  }

  // 保存
  @PostMapping("save")
  @TransactionTokenCheck("save")
  public String saveCase(@ModelAttribute("mstCaseForm") MstMatterForm mstCaseForm,
      @RequestParam("test") String testValue, HttpSession session, Model model) {


    logger.info("--- CaseController.saveCase START ---");

    if ("hoge1".equals(testValue)) {

      // 訪問担当者
      List<MstUser> user = mstUserService.findAllUsersWithAuthName();
      model.addAttribute("visitUser", user);

      // 顧客情報
      List<MstCustomer> customer = mstCustomerService.getAllCustomers();
      model.addAttribute("customer", customer);
      model.addAttribute("inputCustomer", new MstCustomer());

      model.addAttribute("bukken", mstFloorManagementService.viewFloorForm());

      String transactionToken = UUID.randomUUID().toString();
      session.setAttribute("transactionToken", transactionToken);
      model.addAttribute("transactionToken", transactionToken);
      // 都道府県のリスト
      model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));

      // 種別を取得するためのリスト
      model.addAttribute("caseKind", mstCodeService.getCodeByKind(TASK_SUBSTANCE));

      model.addAttribute("mstCase", mstCaseForm);

      return "case/case_register";
    }
    mstMatterService.saveCase(mstCaseForm);

    logger.info("--- CaseController.saveCase END ---");
    return "redirect:/case/save";
  }

  @GetMapping("save")
  public String createCompleteRegister() {
    return "case/case_register_end";
  }

  @GetMapping("select")
  public String bukkenSelect(HttpSession session, Model model) {
    model.addAttribute("bukken", mstMatterService.selectForm());

    return "case/case_bukken_select";
  }

  // 変更
  @GetMapping("update")
  public String updateCaseForm(@RequestParam(value = "id") int id, HttpSession session,
      Model model) {

    logger.info("--- CustomerController.updateCaseForm START ---");

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    List<MstCustomer> customer = mstCustomerService.getAllCustomers();
    model.addAttribute("customer", customer);

    model.addAttribute("bukken", mstFloorManagementService.viewFloorForm());



    // 都道府県のリスト
    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));

    // 種別を取得するためのリスト
    model.addAttribute("caseKind", mstCodeService.getCodeByKind(TASK_SUBSTANCE));


    MstMatter mstCase = mstMatterService.findDisplayById(id);

    // // 日付のフォーマットを変更する処理
    // String formattedCaseVisitPlanDate =
    // this.commonService.getDatetimeLocalFormatString(mstCase.getCaseVisitPlanDate());
    // mstCase.setStrCaseVisitPlanDate(formattedCaseVisitPlanDate);
    //
    // String formattedStrCaseContractDate =
    // this.commonService.getDatetimeLocalFormatString(mstCase.getCaseContractDate());
    // mstCase.setStrCaseContractDate(formattedStrCaseContractDate);
    //
    // String formattedStrCaseContractEndDate =
    // this.commonService.getDatetimeLocalFormatString(mstCase.getCaseContractEndDate());
    // mstCase.setStrCaseContractEndDate(formattedStrCaseContractEndDate);

    model.addAttribute("mstCase", mstMatterService.updateCaseForm(mstCase));

    // 訪問担当者
    List<MstUser> user = mstUserService.findAllUsersWithAuthName();
    model.addAttribute("visitUser", user);

    logger.info("--- CustomerController.updateCaseForm END ---");

    return "case/case_update";
  }

  @PostMapping("update")
  public String processUpdate(
      @ModelAttribute("mstCase") @Validated(ValidationGroups.Registration.class) MstMatterForm mstCaseForm,
      BindingResult result, HttpSession session, Model model) {

    logger.info("--- CaseController.processUpdate START ---");

    if (result.hasErrors()) {

      List<MstCustomer> customer = mstCustomerService.getAllCustomers();
      model.addAttribute("customer", customer);
      model.addAttribute("inputCustomer", new MstCustomer());

      model.addAttribute("bukken", mstFloorManagementService.viewFloorForm());

      String transactionToken = UUID.randomUUID().toString();
      session.setAttribute("transactionToken", transactionToken);
      model.addAttribute("transactionToken", transactionToken);
      // 都道府県のリスト
      model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));

      // 種別を取得するためのリスト
      model.addAttribute("caseKind", mstCodeService.getCodeByKind(TASK_SUBSTANCE));

      // 訪問担当者
      List<MstUser> user = mstUserService.findAllUsersWithAuthName();
      model.addAttribute("visitUser", user);

      return "case/case_update";
    }
    if (mstCaseForm.getCustomerId() != null) {
      mstCaseForm.setCustomerName(
          mstCustomerService.getCustomerById(mstCaseForm.getCustomerId()).getCorpName());
      model.addAttribute("mstCaseForm", mstCaseForm);
    } else {
      mstCaseForm.setCustomerName(mstCustomerService.getCustomerById(1).getCorpName());
      model.addAttribute("mstCaseForm", mstCaseForm);
    }
    // 訪問担当者
    if (mstCaseForm.getVisitId() != null) {

      String fName = mstUserService.getUserById(mstCaseForm.getVisitId()).getFName();
      String lName = mstUserService.getUserById(mstCaseForm.getVisitId()).getLName();

      String visitName = (fName + lName);

      mstCaseForm.setVisitName(visitName);
      model.addAttribute("mstCaseForm", mstCaseForm);
    } else {
      mstCaseForm.setVisitName(null);
      model.addAttribute("mstCaseForm", mstCaseForm);
    }



    return "case/case_update_confirm";
  }

  // 更新保存
  @PostMapping("save-update")
  @TransactionTokenCheck("save-update")
  public String saveCaseUpdate(@ModelAttribute("mstCaseForm") MstMatterForm mstCaseForm) { // DBに登録

    logger.info("--- CaseController.saveCustomer START ---");

    mstMatterService.saveCase(mstCaseForm);

    return "redirect:/case/save-update";
  }


  @GetMapping("save-update")
  public String createCompleteUpdate() { // 変更完了画面
    return "case/case_update_end";
  }


  @GetMapping("poi")
  public void generateExcel(HttpServletResponse response) throws IOException {

  }

  @GetMapping("search")
  public String search(HttpServletRequest request, Model model,
      @ModelAttribute("search") MstMatterForm mstCaseForm) {
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());

    model.addAttribute("caseInfo", mstMatterService.search(mstCaseForm));
    // 検索用
    model.addAttribute("search", mstMatterService.registerCaseForm());

    model.addAttribute("bool", "true");
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    // 状況ステータスを取得するためのリスト
    model.addAttribute("situation", mstCodeService.getCodeByKind(SITUATION_STATUS));
    model.addAttribute("mstCase", mstMatterService.registerCaseForm());
    // 種別を取得するためのリスト
    model.addAttribute("caseKind", mstCodeService.getCodeByKind(TASK_SUBSTANCE));
    return "case/case";
  }

  @GetMapping("copy")
  public String copy(@RequestParam(value = "id") int id,
      @RequestParam(value = "estimateVer", required = false) String estimateVer, Model model,
      HttpSession session) throws InvocationTargetException {
    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);
    // 案件
    MstMatter mstCase = mstMatterService.findDisplayById(id);
    model.addAttribute("mstMatter", mstMatterService.updateCaseForm(mstCase));

    // 見積
    Integer ver; // バージョン

    Integer newVer = null; // 最新バージョン

    // 初期表示で最新バージョンを表示するための判定
    if (estimateVer == null && mstEstimateManagementService.newestVarsion(id) != null) {
      newVer = mstEstimateManagementService.newestVarsion(id);
    }


    ver = (estimateVer != null) ? Integer.parseInt(estimateVer) : 1; // リクエストパラムで持ってきた値を代入

    // 見積もりがあるかを判定
    if (mstEstimateItemService.findAllById(id).size() != 0) {

      model.addAttribute("ver", mstEstimateItemService.findAllById(id)); // 案件に対するすべてのバージョンを取得


      // 初期表示(最新バージョン)判定
      if (newVer != null) {
        model.addAttribute("estimate", mstEstimateItemService.findByDisplyById(id, newVer));
        model.addAttribute("estimateVer", newVer);
      } else {
        model.addAttribute("estimate", mstEstimateItemService.findByDisplyById(id, ver)); // バージョンに対する表示内容を取得
        model.addAttribute("estimateVer", ver); // バージョンをviewに受け渡し
      }

      model.addAttribute("id", id); // 案件IDをviewに受け渡し
      return "case/case_copy";
    }
    model.addAttribute("message", "見積もり情報は登録されていません");
    return "case/case_copy";
  }

  @GetMapping("copyConfirm")
  public String copyConfirm(@RequestParam(value = "id") int id, Model model,
      @RequestParam(value = "estimateVer", required = false) String estimateVer,
      HttpSession session) {
    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);
    // 案件
    MstMatter mstCase = mstMatterService.findDisplayById(id);
    model.addAttribute("mstMatter", mstMatterService.updateCaseForm(mstCase));

    // 見積
    if (estimateVer.isEmpty()) { // 見積もりがあるかを判定してメッセージを保存
      model.addAttribute("message", "見積もり情報は登録されていません");
      return "case/case_copy_confirm";
    }
    Integer ver = Integer.parseInt(estimateVer);

    if (ver != null) {
      model.addAttribute("estimate", mstEstimateItemService.findByDisplyById(id, ver));
    }
    return "case/case_copy_confirm";
  }

  @PostMapping("save-copy")
  @TransactionTokenCheck("save-copy")
  public String saveCaseCopy(@RequestParam(name = "id") int id,
      @RequestParam(value = "estimateVer", required = false) String estimateVer) { // DBに登録

    logger.info("--- CaseController.saveCustomer START ---");

    // 見積もりがなかった場合の処理（案件のみ登録）
    if (estimateVer == null) {
      MstMatter mstCase = mstMatterService.findDisplayById(id);
      mstMatterService.saveCopy(mstMatterService.updateCaseForm(mstCase), estimateAbsence);
      return "redirect:/case/save-copy";
    }


    Integer ver = Integer.parseInt(estimateVer);

    // 案件
    MstMatter mstCase = mstMatterService.findDisplayById(id);
    MstMatter result = mstMatterService.saveCopy(mstMatterService.updateCaseForm(mstCase), ver);



    mstEstimateItemService.saveCopy(mstEstimateItemService.findByDisplyById(id, ver), result);


    return "redirect:/case/save-copy";
  }

  @GetMapping("save-copy")
  public String saveCaseCopy() {
    return "case/case_register_end";
  }

}
