package com.seproject.buildmanager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import com.seproject.buildmanager.entity.MstCheckItemConstructionClassificationManagement;
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.service.MstCheckItemConstructionClassificationManagementService;
import com.seproject.buildmanager.service.MstCustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("check-cost")
public class CheckItemConstructionClassificationManagementController {
  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstCheckItemConstructionClassificationManagementService mstCheckCostService;


  private final MstCustomerService mstCustomerService;

  public CheckItemConstructionClassificationManagementController(
      MstCheckItemConstructionClassificationManagementService mstCheckCostService,
      MstCustomerService mstCustomerService) {
    super();
    this.mstCheckCostService = mstCheckCostService;
    this.mstCustomerService = mstCustomerService;
  }

  @GetMapping("")
  public String getAllCheckCost(HttpServletRequest request, Model model,
      @RequestParam(name = "checkId", required = false) String checkId) {
    // logger.info("--- OwnerController.getAllOwner START ---");

    // checkIdが指定されている場合は対象の顧客IDを持つ
    Integer intcheckId;
    intcheckId = (checkId != null) ? Integer.parseInt(checkId) : 0;

    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
    model.addAttribute("customer", mstCheckCostService.getCustmerAll());
    // グループ
    model.addAttribute("check", mstCheckCostService.getCheckAll());
    model.addAttribute("cost", mstCheckCostService.getCostAll());
    model.addAttribute("construction", mstCheckCostService.getConstuctionAll());
    // セレクトボックスに表示する顧客を取得、顧客IDの指定が無く、0件でない場合は先頭のIDを持つ
    List<MstCustomer> customers = this.mstCheckCostService.getCustmerAll();
    if (intcheckId == 0 && !customers.isEmpty()) {
      intcheckId = customers.get(0).getId();
    }
    model.addAttribute("checkcost", mstCheckCostService.searchCustomer(intcheckId));

    model.addAttribute("checkId", intcheckId);


    // logger.info("--- UserController.getAllUsers END ---");
    return "checkItemConstructionClassificationManagement/check_item_construction_classification_management";
  }

  @GetMapping("select")
  public String getALLCheckCostSelect(@RequestParam(value = "cId") String cId,
      @RequestParam(value = "checkId") String checkId, HttpSession session, Model model) {
    // 引数が未指定の場合はチェック項目工事区分管理画面に戻す
    if (!StringUtils.hasText(cId) || !StringUtils.hasText(checkId)) {
      return "redirect:/check_item_construction_classification_management";
    }

    // 引数が数字でない場合はチェック項目グループ管理画面に戻す
    String pattern = "[0-9]+";
    if (!cId.matches(pattern) || !checkId.matches(pattern)) {
      return "redirect:/check_item_construction_classification_management";
    }

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);
    model.addAttribute("customer", mstCheckCostService.getCustmerAll());
    // グループ
    model.addAttribute("check", mstCheckCostService.getCheckAll());
    model.addAttribute("cost", mstCheckCostService.getCostAll());
    model.addAttribute("construction", mstCheckCostService.getConstuctionAll());
    model.addAttribute("checkcost",
        new ArrayList<MstCheckItemConstructionClassificationManagement>());

    model.addAttribute("customerId", checkId);
    model.addAttribute("checkId", cId);


    MstCustomer mstCustomer = mstCustomerService.getCustomerById(Integer.parseInt(checkId));
    model.addAttribute("mstCheckCost", mstCustomer);

    Map<Integer, MstCheckItemConstructionClassificationManagement> checkCustomerMap =
        new HashMap<Integer, MstCheckItemConstructionClassificationManagement>();

    List<MstCheckItemConstructionClassificationManagement> checkCustomer =
        mstCheckCostService.checkcustomer(Integer.parseInt(checkId), Integer.parseInt(cId));
    int i = 1;
    for (MstCheckItemConstructionClassificationManagement checkCost : checkCustomer) {
      checkCustomerMap.put(i, checkCost);
      i++;
    }
    model.addAttribute("checkcustomer", checkCustomerMap);

    model.addAttribute("checkCustomerNum", checkCustomer.size());

    return "checkItemConstructionClassificationManagement/check_item_construction_classification_management_select";
  }

  @PostMapping("save")
  public String addItems(@RequestParam(value = "checkbox") String checkBox,
      @RequestParam(value = "costgroupname") String costName,
      @RequestParam(value = "costcontents") String constents,
      @RequestParam(value = "checkcostId") String checkcostId,
      @RequestParam(value = "customerId") int customerid,
      @RequestParam(value = "checkId") String checkid, HttpSession session, Model model) {

    String[] checkBoxs = checkBox.split(",");
    String[] costNames = costName.split(",");
    String[] constent = constents.split(",");
    String[] ccid = checkcostId.split(",");



    mstCheckCostService.saveCheck(ccid, checkBoxs, costNames, constent, checkid, customerid);

    String transactionToken = UUID.randomUUID().toString();
    session.setAttribute("transactionToken", transactionToken);
    model.addAttribute("transactionToken", transactionToken);
    model.addAttribute("customer", mstCheckCostService.getCustmerAll());
    // グループ
    model.addAttribute("check", mstCheckCostService.getCheckAll());
    model.addAttribute("cost", mstCheckCostService.getCostAll());
    model.addAttribute("construction", mstCheckCostService.getConstuctionAll());
    model.addAttribute("checkcost",
        new ArrayList<MstCheckItemConstructionClassificationManagement>());

    model.addAttribute("customerId", customerid);
    model.addAttribute("checkId", checkid);


    MstCustomer mstCustomer = mstCustomerService.getCustomerById(customerid);
    model.addAttribute("mstCheckCost", mstCustomer);


    Map<Integer, MstCheckItemConstructionClassificationManagement> checkCustomerMap =
        new HashMap<Integer, MstCheckItemConstructionClassificationManagement>();

    List<MstCheckItemConstructionClassificationManagement> checkCustomer =
        mstCheckCostService.checkcustomer(customerid, Integer.parseInt(checkid));
    int i = 1;
    for (MstCheckItemConstructionClassificationManagement checkCost : checkCustomer) {
      checkCustomerMap.put(i, checkCost);
      i++;
    }
    model.addAttribute("checkcustomer", checkCustomerMap);

    model.addAttribute("checkCustomerNum", checkCustomer.size());
    return "checkItemConstructionClassificationManagement/check_item_construction_classification_management_select";
  }

  public void setSaveAttributes(int customerid, String checkid, HttpSession session, Model model) {

  }


}


