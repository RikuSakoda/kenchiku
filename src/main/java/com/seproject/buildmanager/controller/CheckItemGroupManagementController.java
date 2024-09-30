package com.seproject.buildmanager.controller;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.seproject.buildmanager.entity.MstCheck;
import com.seproject.buildmanager.entity.MstCheckItemGroupManagement;
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.service.CheckItemGroupManagementService;
import com.seproject.buildmanager.service.MstCheckChangeService;
import com.seproject.buildmanager.service.MstCustomerService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * チェック項目グループ管理を担当するコントローラークラスです。 このクラスはチェック項目グループとそのチェック項目の一覧表示、新規登録、変更を行います。
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
@RequestMapping("check-item-group-management")
public class CheckItemGroupManagementController {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final CheckItemGroupManagementService checkItemGroupManagementService;
  private final MstCheckChangeService mstCheckChangeService;
  private final MstCustomerService mstCustomerService;

  // コンストラクタ
  public CheckItemGroupManagementController(
      CheckItemGroupManagementService checkItemGroupManagementService,
      MstCheckChangeService mstCheckChangeService, MstCustomerService mstCustomerService) {
    super();
    this.checkItemGroupManagementService = checkItemGroupManagementService;
    this.mstCheckChangeService = mstCheckChangeService;
    this.mstCustomerService = mstCustomerService;
  }

  /**
   * チェック項目グループ管理画面を表示します。
   * 
   * @param request HTTPリクエストオブジェクト
   * @param model モデルオブジェクト
   * @param customerId 顧客ID
   * @return チェック項目グループ管理画面のテンプレート名
   */
  @GetMapping("")
  public String getAllCheckItemGroup(HttpServletRequest request, Model model,
      @RequestParam(name = "customerId", required = false) String customerId) {

    logger.info("--- CheckItemGroupManagementController.getAllCheckItemGroup START ---");

    // customerIdが指定されている場合は対象の顧客IDを持つ
    Integer intCustomerId;
    intCustomerId = (customerId != null) ? Integer.parseInt(customerId) : 0;

    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());

    // セレクトボックスに表示する顧客を取得、顧客IDの指定が無く、0件でない場合は先頭のIDを持つ
    List<MstCustomer> customers = this.checkItemGroupManagementService.getCustmerAll();
    if (intCustomerId == 0 && !customers.isEmpty()) {
      intCustomerId = customers.get(0).getId();
    }
    model.addAttribute("customers", customers);
    model.addAttribute("customerId", intCustomerId);

    // 一覧に表示するチェック項目グループを取得
    model.addAttribute("groups",
        this.checkItemGroupManagementService.getGroupNameByCustomerId(intCustomerId));

    logger.info("--- CheckItemGroupManagementController.getAllCheckItemGroup END ---");

    return "checkItemGroupManagement/check_item_group_management";
  }

  /**
   * チェック項目管理画面を表示します。
   * 
   * @param request HTTPリクエストオブジェクト
   * @param model モデルオブジェクト
   * @param groupId チェック項目グループID
   * @param customerId 顧客ID
   * @return チェック項目管理画面のテンプレート名
   */
  @GetMapping("/item-select")
  public String itemSelect(HttpServletRequest request, Model model,
      @RequestParam(name = "groupId", required = false) String groupId,
      @RequestParam(name = "customerId", required = false) String customerId) {

    logger.info("--- CheckItemGroupManagementController.itemSelect START ---");

    // 引数が未指定の場合はチェック項目グループ管理画面に戻す
    if (!StringUtils.hasText(groupId) || !StringUtils.hasText(customerId)) {
      return "redirect:/check-item-group-management";
    }

    // 引数が数字でない場合はチェック項目グループ管理画面に戻す
    String pattern = "[0-9]+";
    if (!groupId.matches(pattern) || !groupId.matches(pattern)) {
      return "redirect:/check-item-group-management";
    }

    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());

    // 引数で絞り込んだチェック項目の状態とセットでチェック項目を取得する
    Integer tmpg = Integer.parseInt(groupId);
    Integer tmpc = Integer.parseInt(customerId);
    List<MstCheck> m =
        this.checkItemGroupManagementService.getCheckItemByGroupIdAndCustomerId(tmpg, tmpc);
    model.addAttribute("check", m);

    model.addAttribute("group", this.mstCheckChangeService.findById(tmpg));
    model.addAttribute("customer", this.mstCustomerService.getCustomerById(tmpc));

    model.addAttribute("groupId", groupId);
    model.addAttribute("customerId", customerId);

    logger.info("--- CheckItemGroupManagementController.itemSelect END ---");

    return "checkItemGroupManagement/check_item_group_management_select";
  }

  /**
   * チェック項目を顧客、チェック項目グループと紐づけます
   * 
   * @param redirectAttributes リダイレクト時に使用する属性
   * @param checkboxs チェックボックスの値のリスト(チェックされたものだけがPOSTされる点に注意
   * @param groupId チェック項目グループID
   * @param customerId 顧客ID
   * @return チェック項目管理画面へリダイレクト
   */
  @PostMapping("/item-select")
  public String itemRegistration(RedirectAttributes redirectAttributes,
      @RequestParam(name = "checkboxs", required = false) List<String> checkboxs,
      @RequestParam("groupId") Integer groupId, @RequestParam("customerId") Integer customerId) {

    logger.info("--- CheckItemGroupManagementController.itemRegistration START ---");

    // 既存の登録を全削除
    this.checkItemGroupManagementService.deleteCheckItemGroup(groupId, customerId);

    // 全てチェックされていない場合はnullになる
    if (checkboxs != null) {

      // チェックされたものだけが渡されるので全登録
      List<MstCheckItemGroupManagement> items = new ArrayList<>();
      for (int i = 0; i < checkboxs.size(); i++) {
        MstCheckItemGroupManagement item = new MstCheckItemGroupManagement();
        item.setGroupId(groupId);
        item.setCheckId(Integer.parseInt(checkboxs.get(i)));
        item.setCustomerId(customerId);
        items.add(item);
      }
      this.checkItemGroupManagementService.saveAll(items);

    }

    // 完了後のリダイレクト先を指定するためにパラメータをリダイレクト要素に設定
    redirectAttributes.addAttribute("groupId", groupId);
    redirectAttributes.addAttribute("customerId", customerId);

    // 完了ダイアログを表示する判定のためにflashに値を持たせる
    redirectAttributes.addFlashAttribute("message", "正常に登録されました。");

    logger.info("--- CheckItemGroupManagementController.itemRegistration END ---");

    return "redirect:/check-item-group-management/item-select";
  }
}
