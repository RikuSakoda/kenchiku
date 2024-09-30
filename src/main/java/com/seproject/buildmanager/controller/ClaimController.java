package com.seproject.buildmanager.controller;

import java.util.List;
import java.util.UUID;
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
import com.seproject.buildmanager.common.MstCodeEnums;
import com.seproject.buildmanager.config.TransactionTokenCheck;
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.entity.MstOwnerManagement;
import com.seproject.buildmanager.form.MstMatterForm;
import com.seproject.buildmanager.repository.MstMatterRepository;
import com.seproject.buildmanager.repository.MstCodeRepository;
import com.seproject.buildmanager.service.MstMatterService;
import com.seproject.buildmanager.service.MstCodeService;
import com.seproject.buildmanager.service.MstCustomerService;
import com.seproject.buildmanager.service.MstOwnerManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("claim")
public class ClaimController {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  @Autowired
  private MstMatterService mstCaseService;// サービスのインスタンス

  @Autowired
  private MstCodeRepository mstCodeRepository;// コードマスタ

  @Autowired
  private MstMatterRepository mstCaseRepository;

  @Autowired
  private MstCustomerService mstCustomerService;

  @Autowired
  private MstOwnerManagementService mstOwnerService;

  @Autowired
  private MstCodeService mstCodeService;

  // Enumから都道府県のcode_kindの値を取得
  private static final int PREFECTURES = MstCodeEnums.PREFECTURES.getValue();

  // Enumから種別を取得するためののcode_kindの値を取得
  private static final int TASK_SUBSTANCE = MstCodeEnums.TASK_SUBSTANCE.getValue();



  public ClaimController(MstMatterService mstClaimService) {
    super();
    this.mstCaseService = mstClaimService;
  }

  @GetMapping("")
  public String getCase(Model model, HttpServletRequest request) {
    logger.info("--- ClaimController.getClaim START ---");
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    // model.addAttribute("caseInfo", mstCaseService.viewClaimForm());


    return "claim/claim";

  }

  @GetMapping("register")
  public String registerClaimForm(HttpSession session, Model model) { // 新規登録画面

    logger.info("--- ClaimController.registerCaseForm START ---");

    List<MstCustomer> customer = mstCustomerService.getAllCustomers();
    model.addAttribute("customer", customer);

    List<MstOwnerManagement> owner = mstOwnerService.getAllOwner();
    model.addAttribute("owner", owner);

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    // 都道府県のリスト
    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));

    // 種別を取得するためのリスト
    model.addAttribute("caseKind", mstCodeService.getCodeByKind(TASK_SUBSTANCE));

    // model.addAttribute("mstClaim", mstClaimService.registerClaimForm());

    logger.info("--- ClaimController.registerClaimForm End ---");

    return "claim/anken_register";

  }

  // @PostMapping("register")
  // public String processRegistration(
  // @ModelAttribute("mstClaim") @Validated(ValidationGroups.Registration.class) MstCaseForm
  // mstClaimForm,
  // BindingResult bingingResult, HttpSession session, Model model) {// 新規登録確認画面
  //
  // logger.info("--- FloorManagementController.processRegistration START ---");
  //
  // if (bingingResult.hasErrors()) {
  //
  // List<MstCustomer> customer = mstCustomerService.getAllCustomers();
  // model.addAttribute("customer", customer);
  // model.addAttribute("inputCustomer", new MstCustomer());
  //
  // String transactionToken = UUID.randomUUID().toString();
  // session.setAttribute("transactionToken", transactionToken);
  // model.addAttribute("transactionToken", transactionToken);
  // // 都道府県のリスト
  // model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
  // return "case/anken_register";
  // }
  //
  // if (mstCaseForm.getCustId() != null) {
  // mstCaseForm.setCustomerName(mstCustomerService
  // .getCustomerById(Integer.parseInt(mstCaseForm.getCustId())).getCorpName());
  // model.addAttribute("mstCaseForm", mstCaseForm);
  // } else {
  // mstCaseForm.setCustomerName(mstCustomerService.getCustomerById(1).getCorpName());
  // model.addAttribute("mstCaseForm", mstCaseForm);
  // }
  // String owner = mstCaseForm.getOwnId();
  // int ownId = Integer.parseInt(owner);
  // MstOwner own = mstOwnerService.getOwnerId(ownId);
  // mstCaseForm.setVisitManager(own.getLName() + own.getFName());
  //
  //
  //
  // logger.info("--- FloorManagementController.processRegistration END ---");
  //
  // return "case/anken_register_confirm.html";
  //
  //
  // }

  // 保存
  @PostMapping("save")
  @TransactionTokenCheck("save")
  public String saveCase(@ModelAttribute("mstCaseForm") MstMatterForm mstcCaseForm) {

    logger.info("--- CaseController.saveCase START ---");

    mstCaseService.saveCase(mstcCaseForm);

    logger.info("--- CaseController.saveCase END ---");
    return "redirect:/case/save";
  }

  @GetMapping("save")
  public String createCompleteRegister() {
    return "claim/anken_register_end";
  }



}
