package com.seproject.buildmanager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.common.MstCodeEnums;
import com.seproject.buildmanager.entity.MstMatter;
import com.seproject.buildmanager.form.MstAcceptingOrderDetailForm;
import com.seproject.buildmanager.form.MstAcceptingOrderForm;
import com.seproject.buildmanager.form.MstMatterForm;
import com.seproject.buildmanager.service.MstAcceptingOrderService;
import com.seproject.buildmanager.service.MstCodeService;
import com.seproject.buildmanager.service.MstEstimateItemService;
import com.seproject.buildmanager.service.MstEstimateManagementService;
import com.seproject.buildmanager.service.MstMaterialsManagementService;
import com.seproject.buildmanager.service.MstMatterService;
import com.seproject.buildmanager.service.MstSupplierManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("accepting-order")
public class AcceptingOrderController { // OrderManagement

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstCodeService mstCodeService;

  private final MstAcceptingOrderService mstOrderService;

  private final MstMatterService mstCaseService;

  private final MstEstimateItemService mstEstimateItemService;

  private final MstSupplierManagementService mstSupplierService;

  private final MstMaterialsManagementService mstMaterialsManagementService;

  private final MstEstimateManagementService mstEstimateService;

  // Enumから種別を取得するためののcode_kindの値を取得
  private static final int TASK_SUBSTANCE = MstCodeEnums.TASK_SUBSTANCE.getValue();

  // Enumから状況ステータスを取得するため
  private static final int SITUATION_STATUS = MstCodeEnums.SITUATION_STATUS.getValue();

  private static final int ORDER_STATUS = MstCodeEnums.ORDER_STATUS.getValue();

  private static final int ACCEPTING_ORDERS_STATUS =
      MstCodeEnums.ACCEPTING_ORDERS_STATUS.getValue();

  public AcceptingOrderController(MstCodeService mstCodeService,
      MstAcceptingOrderService mstOrderService, MstMatterService mstCaseService,
      MstEstimateItemService mstEstimateItemService,
      MstSupplierManagementService mstSupplierService,
      MstMaterialsManagementService mstMaterialsManagementService,
      MstEstimateManagementService mstEstimateService) {
    this.mstCodeService = mstCodeService;
    this.mstOrderService = mstOrderService;
    this.mstCaseService = mstCaseService;
    this.mstEstimateItemService = mstEstimateItemService;
    this.mstSupplierService = mstSupplierService;
    this.mstMaterialsManagementService = mstMaterialsManagementService;
    this.mstEstimateService = mstEstimateService;
  }

  // 受注一覧
  @GetMapping("")
  public String getAcceptingOrder(Model model, HttpServletRequest request) {
    logger.info("--- AcceptingOrderController.getAcceptingOrder START ---");
    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("orderFormList", this.mstOrderService.viewOrderForm()); // またはordersData
    model.addAttribute("caseFormList",
        this.mstCaseService.selectViewMatterForm(this.mstCaseService.viewCaseForm()));
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);

    // 検索用
    model.addAttribute("search", mstCaseService.registerCaseForm());

    // 状況ステータスを取得するためのリスト
    model.addAttribute("situations", mstCodeService.getCodeByKind(SITUATION_STATUS));

    // 種別を取得するためのリスト
    model.addAttribute("caseKind", mstCodeService.getCodeByKind(TASK_SUBSTANCE));

    logger.info("--- AcceptingOrderController.getAcceptingOrder END ---");

    return "acceptingOrder/accepting_order";
  }

  // 受注 内訳
  @GetMapping("break-down")
  public String viewUchiwake(@RequestParam(value = "id") int id, Model model, HttpSession session) {
    logger.info("--- AcceptingOrderController.viewUchiwake START ---");

    /*
     * 使わない場合は、消す List<MstCustomer> customers = mstCustomerService.getAllCustomers();
     * model.addAttribute("customerList", customers);
     * 
     * // 種別を取得するためのリスト model.addAttribute("substances",
     * mstCodeService.getCodeByKind(TASK_SUBSTANCE));
     * 
     * // 状況ステータスを取得するためのリスト model.addAttribute("situations",
     * mstCodeService.getCodeByKind(SITUATION_STATUS));
     * 
     */

    MstMatter mstCase = mstCaseService.findDisplayById(id);
    model.addAttribute("mstCaseForm", mstCaseService.updateCaseForm(mstCase));
    model.addAttribute("estimateItem",
        mstEstimateItemService.findByDisplyById(id, mstCase.getEstimatefinalversion()));
    model.addAttribute("Status", mstCodeService.getCodeByKindStatus(ACCEPTING_ORDERS_STATUS));
    model.addAttribute("estimate", mstEstimateService.findByMatterId(id));

    logger.info("--- AcceptingOrderController.viewUchiwake END ---");

    return "acceptingOrder/accepting_order_break_down";
  }

  // 受注 検索
  @GetMapping("search")
  public String search(HttpServletRequest request, Model model,
      @ModelAttribute("search") MstMatterForm mstCaseForm) {

    model.addAttribute("caseInfo", mstCaseService.search(mstCaseForm));
    // 検索用
    model.addAttribute("search", mstCaseService.registerCaseForm());

    // CSRFトークンをモデルに追加
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("orderFormList", this.mstOrderService.viewOrderForm()); // またはordersData
    model.addAttribute("caseFormList", this.mstOrderService.selectMatterBySituationStatus(
        this.mstCaseService.selectViewMatterForm(this.mstCaseService.viewCaseForm())));
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);

    // 状況ステータスを取得するためのリスト
    model.addAttribute("situations", mstCodeService.getCodeByKind(SITUATION_STATUS));

    return "acceptingOrder/accepting_order";
  }

  // 発注一覧
  @GetMapping("order")
  public String getAllCase(@RequestParam(value = "id") String id, Model model,
      HttpServletRequest request) {


    // logger.info("--- UserController.getAllUsers START ---");

    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    // 案件情報
    model.addAttribute("mstCaseForm",
        mstCaseService.updateCaseForm(mstCaseService.findDisplayById(Integer.parseInt(id))));
    // 発注一覧
    model.addAttribute("acceptionOrder",
        mstOrderService.viewAcceptionOrderForm(Integer.parseInt(id)));
    model.addAttribute("orderSituations", mstCodeService.getCodeByKind(ORDER_STATUS));
    // 検索用
    model.addAttribute("search", mstOrderService.showAcceptingOrderForm());
    // logger.info("--- UserController.getAllUsers END ---");
    return "order/list";
  }

  // 発注 詳細
  @GetMapping("order-detail")
  public String orderDetail(@RequestParam(value = "id") int id,
      @RequestParam("orderId") int orderId, @RequestParam("estimateId") int estimateId, Model model,
      HttpServletRequest request) {
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    // 案件情報
    model.addAttribute("mstCaseForm",
        mstCaseService.updateCaseForm(mstCaseService.findDisplayById(id)));
    // 発注一覧
    model.addAttribute("acceptionOrder", mstOrderService.viewAcceptionOrderForm(id));
    // 発注詳細
    model.addAttribute("acceptionOrderDetail",
        mstOrderService.viewAcceptionOrderDetailForm(orderId));
    // 見積
    MstMatter mstCase = mstCaseService.findDisplayById(id);
    model.addAttribute("estimate",
        mstEstimateItemService.findByDisplyById(estimateId, mstCase.getEstimatefinalversion()));
    // 資材メーカー
    model.addAttribute("materials", mstMaterialsManagementService.getAllMaterials());
    // 業者・仕入先
    model.addAttribute("suppliers", mstSupplierService.getAllSupplierManagement());
    return "order/detail";
  }

  // @GetMapping("select")
  // public String materialsSelect(HttpSession session, Model model) {
  // model.addAttribute("materials", mstMaterialsManagementService.getAllMaterials());
  // return "order/materials_select";
  // }


  // 発注 検索
  @GetMapping("order-search")
  public String orderSearch(@RequestParam(name = "id") String id, HttpServletRequest request,
      Model model, @ModelAttribute("search") MstAcceptingOrderForm mstAcceptingOrderForm) {

    // logger.info("--- UserController.getAllUsers START ---");

    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    // 案件情報
    model.addAttribute("mstCaseForm",
        mstCaseService.updateCaseForm(mstCaseService.findDisplayById(Integer.parseInt(id))));
    // 発注一覧
    model.addAttribute("acceptionOrder",
        mstOrderService.orderSearch(mstAcceptingOrderForm, Integer.parseInt(id)));
    model.addAttribute("orderSituations", mstCodeService.getCodeByKind(ORDER_STATUS));
    // 検索用
    model.addAttribute("search", mstOrderService.showAcceptingOrderForm());

    // logger.info("--- UserController.getAllUsers END ---");
    return "order/list";
  }

  // 発注 発注書作成
  @GetMapping("/make-order")
  public String makeOrder(@RequestParam(name = "id") int id, Model model, HttpSession session) {

    MstMatter mstCase = mstCaseService.findDisplayById(id);
    model.addAttribute("mstCaseForm", mstCaseService.updateCaseForm(mstCase));
    model.addAttribute("estimateItem",
        mstEstimateItemService.findByDisplyById(id, mstCase.getEstimatefinalversion()));
    model.addAttribute("Status", mstCodeService.getCodeByKindStatus(ACCEPTING_ORDERS_STATUS));
    model.addAttribute("supplier", mstSupplierService.viewSupplierForm());
    model.addAttribute("supplierForm", mstSupplierService.showSupplierManagementForm());
    model.addAttribute("orderForm", mstOrderService.showAcceptingOrderForm());
    model.addAttribute("materialsForm", mstMaterialsManagementService.showMaterialsForm());
    model.addAttribute("materials", mstMaterialsManagementService.getAllMaterials());
    model.addAttribute("orderDitailForm", mstOrderService.showAcceptingOrderDetailForm());

    return "order/makeOrder";
  }

  // 発注 発注書作成確認
  @PostMapping("/make-order-confirm")
  public String makeOrderConfirm(@RequestParam(name = "id") int id,
      @RequestParam("orderDitailForm") MstAcceptingOrderDetailForm mstAcceptingOrderDetailForm,
      Model model, HttpSession session) {
    MstMatter mstCase = mstCaseService.findDisplayById(id);
    model.addAttribute("mstCaseForm", mstCaseService.updateCaseForm(mstCase));
    model.addAttribute("estimateItem",
        mstEstimateItemService.findByDisplyById(id, mstCase.getEstimatefinalversion()));
    model.addAttribute("Status", mstCodeService.getCodeByKindStatus(ACCEPTING_ORDERS_STATUS));
    model.addAttribute("supplier", mstSupplierService.viewSupplierForm());
    model.addAttribute("supplierForm", mstSupplierService.showSupplierManagementForm());
    model.addAttribute("orderForm", mstOrderService.showAcceptingOrderForm());
    model.addAttribute("materialsForm", mstMaterialsManagementService.showMaterialsForm());
    model.addAttribute("materials", mstMaterialsManagementService.getAllMaterials());
    model.addAttribute("orderDitailForm", mstAcceptingOrderDetailForm);
    return "acceptingOrder/make_order_confirm";
  }


}
