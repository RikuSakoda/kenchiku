package com.seproject.buildmanager.controller;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import com.seproject.buildmanager.config.TransactionTokenCheck;
import com.seproject.buildmanager.entity.MstUser;
import com.seproject.buildmanager.form.MstUserForm;
import com.seproject.buildmanager.service.MstAuthService;
import com.seproject.buildmanager.service.MstUserService;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * ユーザー管理を担当するコントローラークラスです。 このクラスはユーザーの一覧表示、新規登録、変更を行います。
 * 
 * <p>
 * 変更履歴：
 * <ul>
 * <li>2024/10/31 - 初版作成</li>
 * </ul>
 * 
 * @since 1.0
 * @version 1.0
 */
@Controller
@RequestMapping("user")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  @Autowired
  private MstUserService mstUserService;

  @Autowired
  private MstAuthService mstAuthService;

  public UserController(MstUserService mstUserService, MstAuthService mstAuthService) {
    super();
    this.mstUserService = mstUserService;
    this.mstAuthService = mstAuthService;
  }

  /**
   * ユーザー一覧画面を表示します。
   * 
   * @param request HTTPリクエストオブジェクト
   * @param model モデルオブジェクト
   * @param search 検索条件のパラメータ
   * @return ユーザー一覧画面のテンプレート名
   */
  @GetMapping("")
  public String getAllUsers(HttpServletRequest request, Model model,
      @RequestParam(name = "search", required = false) String search) {
    logger.info("--- UserController.getAllUsers START ---");

    // URLに検索条件のパラメータを持つ場合は検索用テキストボックスに初期表示したい
    if (search != null) {
      model.addAttribute("searchParam", search);
    } else {
      model.addAttribute("searchParam", "");
    }

    model.addAttribute("users", this.mstUserService.findAllUsersWithAuthName());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);
    model.addAttribute("mstUser", mstUserService.showUserForm());
    model.addAttribute("searchUser", mstUserService.showUserForm());

    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());

    logger.info("--- UserController.getAllUsers END ---");
    return "user/list";
  }

  /**
   * ユーザーの新規登録画面を表示します。
   * 
   * @param session HTTPセッションオブジェクト
   * @param model モデルオブジェクト
   * @return ユーザー登録画面のテンプレート名
   */
  @GetMapping("register")
  public String showUserForm(HttpSession session, Model model) {
    logger.info("--- UserController.showUserForm START ---");

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    model.addAttribute("mstAuths", this.mstAuthService.getAllActiveAuth());
    model.addAttribute("mstUser", this.mstUserService.showUserForm());

    logger.info("--- UserController.showUserForm END ---");
    return "user/register";
  }

  /**
   * ユーザーの新規登録確認画面を表示します。
   * 
   * @param mstUserForm ユーザー登録フォームオブジェクト
   * @param model モデルオブジェクト
   * @return ユーザー登録確認画面のテンプレート名
   */
  @PostMapping("register")
  public String processRegistration(
      @ModelAttribute("mstUser") @Validated(ValidationGroups.Registration.class) MstUserForm mstUserForm,
      BindingResult result, HttpSession session, Model model) {
    logger.info("--- UserController.processRegistration START ---");

    if (result.hasErrors()) {
      String transactionToken = (String) session.getAttribute("transactionToken");
      model.addAttribute("transactionToken", transactionToken);
      model.addAttribute("mstAuths", this.mstAuthService.getAllActiveAuth());
      return "user/register";
    }

    Integer mstAuthId = mstUserForm.getMstAuthId();
    model.addAttribute("mstAuth", this.mstAuthService.findByIdA(mstAuthId));
    model.addAttribute("mstUserForm", mstUserForm);

    logger.info("--- UserController.processRegistration END ---");
    return "user/register_confirm";
  }

  /**
   * ユーザーの新規登録を行います。
   * 
   * @param mstUserForm ユーザー登録フォームオブジェクト
   * @return ユーザー登録完了画面へのリダイレクト
   */
  @PostMapping("save")
  @TransactionTokenCheck("save")
  public String saveUser(@ModelAttribute("mstUserForm") MstUserForm mstUserForm) {
    logger.info("--- UserController.saveUser START ---");

    this.mstUserService.saveUser(mstUserForm);

    logger.info("--- UserController.saveUser END ---");
    return "redirect:/user/save";
  }

  /**
   * ユーザーの新規登録完了画面を表示します。
   * 
   * @return ユーザー登録完了画面のテンプレート名
   */
  @GetMapping("save")
  public String createComplete() {
    return "user/register_end";
  }

  // ----------------------------------------------------

  /**
   * ユーザーの変更画面を表示します。
   * 
   * @param session HTTPセッションオブジェクト
   * @param model モデルオブジェクト
   * @param id 変更対象のユーザーID
   * @return ユーザー変更画面のテンプレート名
   */
  @GetMapping("update")
  public String updateUser(HttpSession session, Model model,
      @RequestParam(value = "id") Integer id) {
    logger.info("--- UserController.updateUser START id:" + id + " ---");

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);

    MstUser mstUser = this.mstUserService.getUserById(id);
    MstUserForm mstUserForm = new MstUserForm();
    BeanUtils.copyProperties(mstUser, mstUserForm);

    model.addAttribute("mstAuths", this.mstAuthService.getAllActiveAuth());
    model.addAttribute("mstUser", mstUserForm);

    logger.info("--- UserController.updateUser END ---");
    return "user/update";
  }

  /**
   * ユーザーの変更確認画面を表示します。
   * 
   * @param mstUserForm ユーザー変更フォームオブジェクト
   * @param model モデルオブジェクト
   * @return ユーザー変更確認画面のテンプレート名
   */
  @PostMapping("update")
  public String processUpdate(
      @ModelAttribute("mstUser") @Validated(ValidationGroups.Update.class) MstUserForm mstUserForm,
      BindingResult result, HttpSession session, Model model) {
    logger.info("--- UserController.processUpdate START ---");

    if (result.hasErrors()) {
      String transactionToken = (String) session.getAttribute("transactionToken");
      model.addAttribute("transactionToken", transactionToken);
      model.addAttribute("mstAuths", this.mstAuthService.getAllActiveAuth());
      return "user/update";
    }
    Integer mstAuthId = mstUserForm.getMstAuthId();
    model.addAttribute("mstAuth", this.mstAuthService.findByIdA(mstAuthId));
    model.addAttribute("mstUserForm", mstUserForm);

    logger.info("--- UserController.processUpdate END ---");
    return "user/update_confirm";
  }

  @PostMapping("poi")
  public String generateExcel(@ModelAttribute("searchUser") MstUserForm mstUserForm) {
    mstUserService.download(mstUserService.search(mstUserForm));
    return "redirect:/user";
  }

  @PostMapping("poi2")
  public String upload() {
    mstUserService.upload();
    return "redirect:/user";
  }

  @GetMapping("search")
  public String search(HttpServletRequest request, Model model,
      @ModelAttribute("mstUser") MstUserForm mstUserForm,
      @RequestParam(name = "search", required = false) String search) {
    model.addAttribute("mstUser", mstUserService.showUserForm());
    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);

    model.addAttribute("users", mstUserService.search(mstUserForm));
    model.addAttribute("searchUser", mstUserForm);

    // URLに検索条件のパラメータを持つ場合は検索用テキストボックスに初期表示したい
    if (search != null) {
      model.addAttribute("searchParam", search);
    } else {
      model.addAttribute("searchParam", "");
    }
    return "user/list";
  }

}
