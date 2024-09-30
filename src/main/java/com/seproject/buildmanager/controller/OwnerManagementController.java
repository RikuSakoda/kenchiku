package com.seproject.buildmanager.controller;

import java.io.IOException;
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
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.common.MstCodeEnums;
import com.seproject.buildmanager.config.LoginUserDetails;
import com.seproject.buildmanager.config.TransactionTokenCheck;
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.entity.MstOwnerManagement;
import com.seproject.buildmanager.form.MstOwnerManagementForm;
import com.seproject.buildmanager.service.MstCodeService;
import com.seproject.buildmanager.service.MstCustomerService;
import com.seproject.buildmanager.service.MstOwnerManagementService;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("owner")
public class OwnerManagementController {
  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstOwnerManagementService mstOwnerService;

  private final MstCustomerService mstCustomerService;

  private final MstCodeService mstCodeService;



  // Enumから都道府県のcode_kindの値を取得
  private static final int PREFECTURES = MstCodeEnums.PREFECTURES.getValue();

  // 同じく法人・個人のcode_kindの値を取得
  private static final int INDIVIDUAL_CORPORATE = MstCodeEnums.INDIVIDUAL_CORPORATE.getValue();

  // 引数付きコンストラクタ
  public OwnerManagementController(MstOwnerManagementService mstOwnerService,
      MstCustomerService mstCustomerService, MstCodeService mstCodeService) {
    super();
    this.mstOwnerService = mstOwnerService;
    this.mstCustomerService = mstCustomerService;
    this.mstCodeService = mstCodeService;

  }

  /**
   * オーナー一覧画面を表示します。
   * 
   * @param request HTTPリクエストオブジェクト
   * @param model モデルオブジェクト
   * @return オーナー一覧画面のテンプレート名
   */
  @GetMapping("")
  public String getAllOwner(HttpServletRequest request, Model model) throws IOException {// オーナー管理画面

    logger.info("--- OwnerController.getAllOwner START ---");
    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    // 表示用
    model.addAttribute("ownerForms", mstOwnerService.viewOwnerForm());
    // ステータス判別用
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    // 検索用
    model.addAttribute("serachOwner", mstOwnerService.showOwnerForm());
    // ダウンロード
    model.addAttribute("downloadOwner", mstOwnerService.showOwnerForm());
    // 都道府県のリスト
    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
    // 法人か個人か
    model.addAttribute("individual", mstCodeService.getCodeByKind(INDIVIDUAL_CORPORATE));

    model.addAttribute("insert", "format_select(${prefectures}, prefectures)");

    logger.info("--- UserController.getAllUsers END ---");
    return "owner/owner";
  }

  /**
   * オーナーの新規登録画面を表示します。
   * 
   * @param session HTTPセッションオブジェクト
   * @param model モデルオブジェクト
   * @return オーナー登録画面のテンプレート名
   */
  @GetMapping("register")
  public String registerOwnerForm(HttpSession session, Model model) {// 新規登録画面

    logger.info("--- OwnerController.registerOwnerForm START ---");

    // 顧客情報一覧
    List<MstCustomer> customer = mstCustomerService.getAllCustomers();
    model.addAttribute("customers", customer);
    // CSRFトークンをモデルに追加
    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    // 都道府県のリスト
    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
    // 法人か個人か
    model.addAttribute("individual", mstCodeService.getCodeByKind(INDIVIDUAL_CORPORATE));
    // 入力用
    model.addAttribute("mstOwnerForm", mstOwnerService.showOwnerForm());

    logger.info("--- OwnerController.registerOwnerForm END ---");

    return "owner/owner_register";
  }

  // @Validated(ValidationGroups.Registration.class)でバリデーションのチェック対象を指定
  // バリデーションの対象になるクラスには@ModelAttribute("名前")による指定も行う
  // (@ModelAttributeで指定した値は自動的にModelに格納されるため、値を保持したまま、次の画面に遷移できる)
  // BindingResult bindingResultにチェックの結果が入る。
  /**
   * オーナーの新規登録確認画面を表示します。
   * 
   * @param request HTTPリクエストオブジェクト
   * @param MstOwnerManagementForm オーナー登録フォームオブジェクト
   * @param session HTTPセッションオブジェクト
   * @param model モデルオブジェクト
   * @return ユーザー登録確認画面のテンプレート名
   */
  @PostMapping("register")
  public String processRegistration(HttpServletRequest request,
      @ModelAttribute("mstOwnerForm") @Validated(ValidationGroups.Registration.class) MstOwnerManagementForm mstOwnerForm,
      BindingResult bindingResult, HttpSession session, Model model) {// 新規登録確認画面


    logger.info("--- OwnerController.processRegistration START ---");
    // bindingResult.hasErrors()でチェックの結果を取得 trueの場合、エラーがあるということになる
    if (bindingResult.hasErrors()) {
      // Getでmodelに値をセットしている場合は、ここでセットしなおす。
      // ただし、入力用のFormクラスは、値をセットしない(ここでセットしなおすと、Formに入っていたエラーメッセージなどの情報も上書きされてしまうから)
      List<MstCustomer> customer = mstCustomerService.getAllCustomers();
      model.addAttribute("customers", customer);

      String transactionToken = UUID.randomUUID().toString();
      session.setAttribute("transactionToken", transactionToken);
      model.addAttribute("transactionToken", transactionToken);

      // 都道府県のリスト
      model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
      // 法人か個人か
      model.addAttribute("individual", mstCodeService.getCodeByKind(INDIVIDUAL_CORPORATE));
      return "owner/owner_register";
    }

    mstOwnerForm.setClient(mstCustomerService
        .getCustomerById(Integer.parseInt(mstOwnerForm.getClientId())).getCorpName());
    model.addAttribute("mstOwnerForm", mstOwnerForm);


    logger.info("--- OwnerController.processRegistration END ---");
    return "owner/owner_register_confirm";
  }

  /**
   * オーナーの新規登録を行います。
   * 
   * @param MstOwnerManagementForm ユーザー登録フォームオブジェクト
   * @return オーナー登録完了画面へのリダイレクト
   */
  @PostMapping("save-register")
  @TransactionTokenCheck("save-register")
  public String saveOwnerRegister(MstOwnerManagementForm mstOwnerForm,
      @AuthenticationPrincipal LoginUserDetails user) {// DBに登録

    logger.info("--- OwnerController.saveOwner START ---");


    mstOwnerService.saveOwnerRegister(mstOwnerForm);

    logger.info("--- OwnerController.saveOwner END ---");
    return "redirect:/owner/save-register";
  }

  /**
   * オーナーの新規登録完了画面を表示します。
   * 
   * @return オーナー登録完了画面のテンプレート名
   */
  @GetMapping("save-register")
  public String createCompleteRegister() {// 新規登録完了画面
    return "owner/owner_register_end";
  }

  // ---------------------------------------------------------------------------
  /**
   * オーナーの変更画面を表示します。
   * 
   * @param id 変更対象のユーザーID
   * @param session HTTPセッションオブジェクト
   * @param model モデルオブジェクト
   * @return オーナー変更画面のテンプレート名
   */
  @GetMapping("update")
  public String updateOwnerForm(@RequestParam(value = "id") String id, HttpSession session,
      Model model) {
    logger.info("--- OwnerController.updateOwnerForm START ---");

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
    model.addAttribute("individual", mstCodeService.getCodeByKind(INDIVIDUAL_CORPORATE));

    List<MstCustomer> customer = mstCustomerService.getAllCustomers();
    model.addAttribute("customers", customer);

    MstOwnerManagement mstOwner = mstOwnerService.findOwnerById(Integer.parseInt(id));
    model.addAttribute("mstOwnerForm", mstOwnerService.findByIdConvertOwnerForm(mstOwner.getId()));
    logger.info("--- OwnerController.updateOwnerForm END ---");

    return "owner/owner_update";
  }

  /**
   * オーナーの変更確認画面を表示します。
   * 
   * @param MstOwnerManagementForm オーナー変更フォームオブジェクト
   * @param model モデルオブジェクト
   * @return ユーザー変更確認画面のテンプレート名
   */
  @PostMapping("update")
  public String processUpdate(
      @ModelAttribute("mstOwnerForm") @Validated(ValidationGroups.Update.class) MstOwnerManagementForm mstOwnerForm,
      BindingResult bindingResult, HttpSession session, Model model) {
    logger.info("--- OwnerController.processUpdate START ---");
    if (bindingResult.hasErrors()) {
      String transactionToken = UUID.randomUUID().toString();
      session.setAttribute("transactionToken", transactionToken);
      model.addAttribute("transactionToken", transactionToken);

      model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
      model.addAttribute("individual", mstCodeService.getCodeByKind(INDIVIDUAL_CORPORATE));

      List<MstCustomer> customer = mstCustomerService.getAllCustomers();
      model.addAttribute("customers", customer);

      return "owner/owner_update";
    }

    model.addAttribute("mstOwnerForm", mstOwnerService.OwnerForm(mstOwnerForm));

    logger.info("--- OwnerController.processUpdate END ---");
    return "owner/owner_update_confirm";
  }

  /**
   * オーナーの変更を行います。
   * 
   * @param MstOwnerManagementForm ユーザー登録フォームオブジェクト
   * @return オーナー変更完了画面へのリダイレクト
   */
  @PostMapping("save-update")
  @TransactionTokenCheck("save-update")
  public String saveOwnerUpdate(
      @ModelAttribute("mstOwnerForm") MstOwnerManagementForm mstOwnerForm) {// DBに登録

    logger.info("--- OwnerController.saveOwner START ---");

    mstOwnerService.saveOwnerUpdate(mstOwnerForm);

    logger.info("--- OwnerController.saveOwner END ---");
    return "redirect:/owner/save-update";
  }

  /**
   * オーナーの変更完了画面を表示します。
   * 
   * @return オーナー変更完了画面のテンプレート名
   */
  @GetMapping("save-update")
  public String createCompleteUpdate() {// 変更完了画面
    return "owner/owner_update_end";
  }

  @PostMapping("download")
  public String generateExcel(@ModelAttribute("downloadOwner") MstOwnerManagementForm mstOwnerForm)
      throws IOException {
    // 削除部分 ExcelFileService<MstOwner> fileService = new ExcelFileService<MstOwner>("オーナーデータ.xlsx");
    List<MstOwnerManagement> owner =
        mstOwnerService.changeOwnerForm(mstOwnerService.search(mstOwnerForm));

    return "redirect:/owner";
  }

  @GetMapping("search")
  public String search(HttpServletRequest request, Model model,
      @ModelAttribute("serachOwner") MstOwnerManagementForm mstOwnerForm) {
    model.addAttribute("serachOwner", mstOwnerService.showOwnerForm());
    model.addAttribute("downloadOwner", mstOwnerService.showOwnerForm());
    // 都道府県のリスト
    model.addAttribute("prefectures", mstCodeService.getCodeByKind(PREFECTURES));
    // 法人か個人か
    model.addAttribute("individual", mstCodeService.getCodeByKind(INDIVIDUAL_CORPORATE));
    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("ownerForms", mstOwnerService.search(mstOwnerForm));
    return "owner/owner";
  }



}

