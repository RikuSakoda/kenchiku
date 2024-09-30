package com.seproject.buildmanager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.common.MstCodeEnums;
import com.seproject.buildmanager.config.LoginUserDetails;
import com.seproject.buildmanager.config.TransactionTokenCheck;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.entity.MstEstate;
import com.seproject.buildmanager.entity.MstFloorManagement;
import com.seproject.buildmanager.entity.MstFloorName;
import com.seproject.buildmanager.entity.MstOwnerManagement;
import com.seproject.buildmanager.form.MstFloorManagementForm;
import com.seproject.buildmanager.service.FloorNameService;
import com.seproject.buildmanager.service.MstCodeService;
import com.seproject.buildmanager.service.MstCustomerService;
import com.seproject.buildmanager.service.MstFloorManagementService;
import com.seproject.buildmanager.service.MstOwnerManagementService;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("floor-management")
public class FloorManagementController {
  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstFloorManagementService mstFloorManagementService;

  private final FloorNameService mstFloorNameService;

  private final MstCodeService mstCodeService;

  private final MstCustomerService mstCustomerService;

  private final MstOwnerManagementService mstOwnerService;

  // Enumから都道府県のcode_kindの値を取得
  private static final int PREFECTURES = MstCodeEnums.PREFECTURES.getValue();

  // 引数付きコンストラクタ
  public FloorManagementController(MstFloorManagementService mstFloorManagementService,
      FloorNameService mstFloorNameService, MstCustomerService mstCustomerService,
      MstCodeService mstCodeService, MstOwnerManagementService mstOwnerService) {
    super();
    this.mstFloorManagementService = mstFloorManagementService;
    this.mstFloorNameService = mstFloorNameService;
    this.mstCustomerService = mstCustomerService;
    this.mstCodeService = mstCodeService;
    this.mstOwnerService = mstOwnerService;
  }

  @GetMapping("")
  public String getAllFloor(Model model, HttpServletRequest request) {
    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("floor", mstFloorManagementService.viewFloorForm());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("mstFloorManagement", mstFloorManagementService.registerFloorForm());

    // 都道府県のリストを取得してHTMLに渡す
    List<MstCode> prefectures = mstCodeService.getCodeByKind(PREFECTURES);
    model.addAttribute("prefectures", prefectures);

    logger.info("--- FloorManagementController.getAllFloor END ---");
    return "floor/floor_management";
  }

  // 登録
  @GetMapping("register")
  public String registerFloorForm(HttpSession session, Model model) {// 新規登録画面

    logger.info("--- FloorManagementController.registerFloorForm START ---");

    List<MstCustomer> customer = mstCustomerService.getAllCustomers();
    model.addAttribute("customer", customer);
    model.addAttribute("inputCustomer", new MstCustomer());

    List<MstOwnerManagement> owner = mstOwnerService.getAllOwner();
    model.addAttribute("owner", owner);
    model.addAttribute("inputConstruction", new MstOwnerManagement());


    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    // 都道府県のリスト
    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));

    model.addAttribute("mstFloorManagement", mstFloorManagementService.registerFloorForm());

    logger.info("--- FloorManagementController.registerFloorForm END ---");

    return "floor/floor_register";
  }


  @PostMapping("register")
  public String processRegistration(
      @ModelAttribute("mstFloorManagement") @Validated(ValidationGroups.Registration.class) MstFloorManagementForm mstFloorManagementForm,
      BindingResult bindingResult, HttpSession session, Model model) {// 新規登録確認画面

    logger.info("--- FloorManagementController.processRegistration START ---");
    // bindingResult.hasErrors()でチェックの結果を取得 trueの場合、エラーがあるということになる
    if (bindingResult.hasErrors()) {
      // Getでmodelに値をセットしている場合は、ここでセットしなおす。
      // ただし、入力用のFormクラスは、値をセットしない(ここでセットしなおすと、Formに入っていたエラーメッセージなどの情報も上書きされてしまうから)
      List<MstCustomer> customer = mstCustomerService.getAllCustomers();
      model.addAttribute("customer", customer);
      model.addAttribute("inputCustomer", new MstCustomer());

      List<MstOwnerManagement> owner = mstOwnerService.getAllOwner();
      model.addAttribute("owner", owner);
      model.addAttribute("inputConstruction", new MstOwnerManagement());


      String transactionToken = UUID.randomUUID().toString();
      session.setAttribute("transactionToken", transactionToken);
      model.addAttribute("transactionToken", transactionToken);

      // 都道府県のリスト
      model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
      return "floor/floor_register";
    }

    mstFloorManagementForm.setCustomerName(mstCustomerService
        .getCustomerById(Integer.parseInt(mstFloorManagementForm.getCustomerId())).getCorpName());
    model.addAttribute("mstFloorManagementForm", mstFloorManagementForm);

    MstOwnerManagement mstOwner =
        mstOwnerService.findOwnerById(Integer.parseInt(mstFloorManagementForm.getOwnerId()));
    mstFloorManagementForm.setOwnerName(mstOwner.getLName() + mstOwner.getFName());
    model.addAttribute("mstFloorManagementForm", mstFloorManagementForm);

    logger.info("--- FloorManagementController.processRegistration END ---");
    return "floor/floor_register_confirm";
  }

  // 保存
  @PostMapping("saveRegister")
  @TransactionTokenCheck("saveRegister")
  public String saveFloorRegister(MstFloorManagementForm mstFloorManagementForm,
      @AuthenticationPrincipal LoginUserDetails user) {// DBに登録

    logger.info("--- FloorManagementController.saveFloorRegister START ---");


    mstFloorManagementService.saveFloorRegister(mstFloorManagementForm);

    logger.info("--- FloorManagementController.saveFloorRegister END ---");
    return "redirect:/floor-management/saveRegister";
  }

  @GetMapping("saveRegister")
  public String createCompleteRegister() {// 新規登録完了画面
    return "floor/floor_register_end";
  }

  // 変更
  @GetMapping("update")
  public String updateFloorForm(@RequestParam(value = "id") String id, HttpSession session,
      Model model) {
    logger.info("--- FloorManagementController.updateFloorForm START ---");

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));

    MstFloorManagement mstFloorManagement =
        mstFloorManagementService.getFloorId(Integer.parseInt(id));
    model.addAttribute("mstFloorManagement",
        mstFloorManagementService.updateFloorForm(mstFloorManagement.getId()));

    List<MstCustomer> customer = mstCustomerService.getAllCustomers();
    model.addAttribute("customer", customer);
    MstCustomer cus = mstCustomerService.getCustomerById(mstFloorManagement.getCustomerId());

    List<MstOwnerManagement> owner = mstOwnerService.getAllOwner();
    model.addAttribute("owner", owner);
    MstOwnerManagement ow = mstOwnerService.findOwnerById(mstFloorManagement.getOwnerId());

    model.addAttribute("mstFloorManagementForm", mstFloorManagementService
        .updateFloorForm(mstFloorManagement, cus.getCorpName(), ow.getLName() + ow.getFName()));

    logger.info("--- FloorManagementController.updateFloorForm END ---");

    return "floor/floor_update";
  }

  @PostMapping("update")
  public String processUpdate(
      @ModelAttribute("mstFloorManagementForm") @Validated(ValidationGroups.Update.class) MstFloorManagementForm mstFloorManagementForm,
      BindingResult bindingResult, HttpSession session, Model model) {
    logger.info("--- FloorManagementController.processUpdate START ---");
    if (bindingResult.hasErrors()) {
      String transactionToken = UUID.randomUUID().toString();
      session.setAttribute("transactionToken", transactionToken);
      model.addAttribute("transactionToken", transactionToken);

      model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));


      List<MstCustomer> customer = mstCustomerService.getAllCustomers();
      model.addAttribute("customer", customer);

      List<MstOwnerManagement> owner = mstOwnerService.getAllOwner();
      model.addAttribute("owner", owner);


      return "floor/floor_update";
    }

    model.addAttribute("mstFloorManagementForm",
        mstFloorManagementService.FloorForm(mstFloorManagementForm));

    logger.info("--- FloorManagementController.processUpdate END ---");
    return "floor/floor_update_confirm";
  }

  // 更新保存
  @PostMapping("save-update")
  @TransactionTokenCheck("saveUpdate")
  public String saveFloorUpdate(
      @ModelAttribute("mstFloorManagementForm") MstFloorManagementForm mstFloorManagementForm) {// DBに登録

    logger.info("--- FloorManagementController.FloorUpdate START ---");

    mstFloorManagementService.saveFloorUpdate(mstFloorManagementForm);

    logger.info("--- FloorManagementController.FloorUpdate END ---");
    return "redirect:/floor-management/save-update";
  }

  @GetMapping("save-update")
  public String createCompleteUpdate() {// 変更完了画面
    return "floor/floor_update_end";
  }

  // 間取り一覧
  @GetMapping("select")
  public String selectFloorForm(@RequestParam(value = "id") String id, HttpSession session,
      Model model, HttpServletRequest request) {

    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    MstFloorManagement mstFloorManagement =
        mstFloorManagementService.getFloorId(Integer.parseInt(id));
    model.addAttribute("mstFloorManagement",
        mstFloorManagementService.selectFloorForm(mstFloorManagement));


    model.addAttribute("mstFloorManagementForm",
        mstFloorManagementService.selectFloorForm(mstFloorManagement));
    model.addAttribute("floorname", mstFloorManagementService.getFloorNameAll());
    model.addAttribute("estate", mstFloorManagementService.viewEstateForm(Integer.parseInt(id)));

    return "floor/floor_layout_select";
  }

  // チェック項目
  @GetMapping("check")
  public String selectFloorCheck(@RequestParam("id") String id, @RequestParam("id2") String id2,
      HttpSession session, Model model, HttpServletRequest request) {

    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("floor", mstFloorManagementService.viewFloorForm());


    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    model.addAttribute("id", id);
    model.addAttribute("id2", id2);


    MstFloorManagement mstFloorManagement =
        mstFloorManagementService.getFloorId(Integer.parseInt(id));
    model.addAttribute("mstFloorManagementForm",
        mstFloorManagementService.selectFloorForm(mstFloorManagement));

    MstFloorName mstFloorName = mstFloorNameService.getFloorId(Integer.parseInt(id));
    model.addAttribute("mstFloorNameForm", mstFloorNameService.selectFloorForm(mstFloorName));

    model.addAttribute("floorname", mstFloorManagementService.getFloorNameAll());
    model.addAttribute("checkgroup", mstFloorManagementService.getCheckGroupAll());

    return "floor/floor_layout_check";
  }

  @PostMapping("check")
  public String saveFloorCheck(@RequestParam("id") String id, @RequestParam("id2") String id2,
      @RequestParam(name = "checkboxs", required = false) List<String> checkboxs,
      RedirectAttributes redirectAttributes) {

    // 既存の登録を全削除
    this.mstFloorManagementService.deleteCheckItemGroup(Integer.parseInt(id),
        Integer.parseInt(id2));

    // 全てチェックされていない場合はnullになる
    if (checkboxs != null) {

      // チェックされたものだけが渡されるので全登録
      List<MstEstate> items = new ArrayList<>();
      for (int i = 0; i < checkboxs.size(); i++) {
        MstEstate item = new MstEstate();
        item.setProId(Integer.parseInt(id));
        item.setCheckId(checkboxs.get(i).toString());
        item.setLayId(Integer.parseInt(id2));
        items.add(item);
      }
      this.mstFloorManagementService.saveAll(items);

    }
    // 完了ダイアログを表示する判定のためにflashに値を持たせる
    redirectAttributes.addFlashAttribute("message", "正常に登録されました。");
    redirectAttributes.addAttribute("id", id);
    redirectAttributes.addAttribute("id2", id2);

    return "redirect:/floor-management/check";

  }

  // 検索
  @GetMapping("search")
  public String search(HttpServletRequest request, Model model,
      @ModelAttribute("mstFloorManagementForm") MstFloorManagementForm mstFloorManagementForm) {
    model.addAttribute("mstFloorManagement", mstFloorManagementService.registerFloorForm());
    // 都道府県のリスト
    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("floor", mstFloorManagementService.search(mstFloorManagementForm));
    return "floor/floor_management";
  }


}
