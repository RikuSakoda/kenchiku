package com.seproject.buildmanager.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.seproject.buildmanager.config.LoginUserDetails;
import com.seproject.buildmanager.config.TransactionTokenCheck;
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.entity.MstYoshino;
import com.seproject.buildmanager.form.MstYoshinoForm;
import com.seproject.buildmanager.service.MstCustomerService;
import com.seproject.buildmanager.service.MstYoshinoService;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("user_yoshino")
public class UserYoshinoController {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  @Autowired
  private MstYoshinoService mstYoshinoService;

  @Autowired
  private MstCustomerService mstCustomerService;

  public UserYoshinoController(MstYoshinoService mstYoshinoService) {
    this.mstYoshinoService = mstYoshinoService;
  }

  @GetMapping("")
  public String getAllUsers(HttpServletRequest request, Model model,
      @RequestParam(name = "search", required = false) String search) {
    logger.info("--- UserYoshinoController.getAllUsers START ---");

    if (search != null) {
      model.addAttribute("searchParam", search);
    } else {
      model.addAttribute("searchParam", "");
    }

    model.addAttribute("users", this.mstYoshinoService.findAll());
    model.addAttribute("statusTrue", Constants.STATUS_TRUE);

    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    model.addAttribute("csrfToken", csrfToken.getToken());
    model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());

    logger.info("--- UserYoshinoController.getAllUsers END ---");
    return "user_yoshino/list";
  }

  @GetMapping("register")
  public String showYoshinoForm(HttpSession session, Model model) {
      logger.info("--- UserYoshinoController.showYoshinoForm START ---");
      
      List<MstCustomer> customer = mstCustomerService.getAllCustomers();
      model.addAttribute("customer", customer);

      String transactionToken = UUID.randomUUID().toString();
      session.setAttribute("transactionToken", transactionToken);
      model.addAttribute("transactionToken", transactionToken);

      model.addAttribute("mstYoshinoForm", new MstYoshinoForm());

      logger.info("--- UserYoshinoController.showUserForm END ---");
      return "user_yoshino/register";
  }

  @PostMapping("register")
  public String processRegistration(HttpServletRequest request,
      @ModelAttribute("mstYoshinoForm") @Validated(ValidationGroups.Registration.class) MstYoshinoForm mstYoshinoForm,
      BindingResult bindingResult, HttpSession session, Model model) {

    logger.info("--- YoshinoController.processRegistration START ---");
    if (bindingResult.hasErrors()) {
      List<MstCustomer> customer = mstCustomerService.getAllCustomers();
      model.addAttribute("customer", customer);

      String transactionToken = UUID.randomUUID().toString();
      session.setAttribute("transactionToken", transactionToken);
      model.addAttribute("transactionToken", transactionToken);

      return "user_yoshino/register";
    }
    
    mstYoshinoForm.setCustcorpname(mstCustomerService
        .getCustomerById(mstYoshinoForm.getCustkind()).getCorpName());
    model.addAttribute("mstYoshinoForm", mstYoshinoForm);

    logger.info("--- YoshinoController.processRegistration END ---");
    return "user_yoshino/register_confirm";
  }
    
  @PostMapping("save-register")
  @TransactionTokenCheck("save-register")
  public String saveYoshinoRegister(MstYoshinoForm mstYoshinoForm,
      @AuthenticationPrincipal LoginUserDetails user) {

    logger.info("--- YoshinoController.saveYoshino START ---");

    mstYoshinoService.saveYoshinoRegister(mstYoshinoForm);

    logger.info("--- YoshinoController.saveYoshino END ---");
    return "redirect:/user_yoshino/save-register";
  }

  @GetMapping("save-register")
  public String createCompleteRegister() {
    return "user_Yoshino/register_end";
  }

  @GetMapping("update")
  public String updateYoshinoForm(@RequestParam(value = "id") String id, HttpSession session, Model model) {
     logger.info("--- UserYoshinoController.updateYoshinoForm START ---");

     String transactionToken = UUID.randomUUID().toString();
     session.setAttribute("transactionToken", transactionToken);
     model.addAttribute("transactionToken", transactionToken);

     List<MstCustomer> customer = mstCustomerService.getAllCustomers();
     model.addAttribute("customer", customer);

     MstYoshino mstYoshino = mstYoshinoService.getYoshinoId(Integer.parseInt(id));
     MstYoshinoForm mstYoshinoForm = mstYoshinoService.updateYoshinoForm(mstYoshino.getId());
     model.addAttribute("mstYoshinoForm", mstYoshinoForm);
     logger.info("--- UserYoshinoController.updateYoshinoForm END ---");

     return "user_yoshino/update";
  }

  @PostMapping("update")
  public String processUpdate(
       @ModelAttribute("mstYoshinoForm") @Validated(ValidationGroups.Update.class) MstYoshinoForm mstYoshinoForm,
       BindingResult bindingResult, HttpSession session, Model model) {
     logger.info("--- UserYoshinoController.processUpdate START ---");
     if (bindingResult.hasErrors()) {
         String transactionToken = UUID.randomUUID().toString();
         session.setAttribute("transactionToken", transactionToken);
         model.addAttribute("transactionToken", transactionToken);

         List<MstCustomer> customer = mstCustomerService.getAllCustomers();
         model.addAttribute("customer", customer);

         return "user_yoshino/update";
     }
     
     model.addAttribute("mstYoshinoForm", mstYoshinoService.YoshinoForm(mstYoshinoForm));
     logger.info("--- UserYoshinoController.processUpdate END ---");
     return "user_yoshino/update_confirm";
  }
  
  @PostMapping("save-update")
  @TransactionTokenCheck("save-update")
  public String saveYoshinoUpdate(MstYoshinoForm mstYoshinoForm,
       @AuthenticationPrincipal LoginUserDetails user) {
     logger.info("--- UserYoshinoController.saveYoshinoUpdate START ---");

     mstYoshinoService.updateYoshino(mstYoshinoForm);

     logger.info("--- UserYoshinoController.saveYoshinoUpdate END ---");
     return "redirect:/user_yoshino/save-update";
  }

  @GetMapping("save-update")
  public String updateComplete() {
     return "user_yoshino/update_end";
  }

//  // ステータス更新用のメソッド
//  @PostMapping("update-status")
//  public ResponseEntity<?> updateStatus(
//      @RequestParam(value = "userId") int userId, 
//      @RequestParam(value = "status") String status) {
//      
//      logger.info("--- UserYoshinoController.updateStatus START ---");
//
//      try {
//          MstYoshino user = mstYoshinoService.getYoshinoId(userId);
//
//          // String型のステータスをInteger型に変換
//          Integer newStatus = "有効".equals(status) ? 1 : 0;
//          user.setStatus(newStatus);
//
//          // MstYoshinoオブジェクトからMstYoshinoFormオブジェクトを作成
//          MstYoshinoForm userForm = new MstYoshinoForm();
//          BeanUtils.copyProperties(user, userForm);
//
//          mstYoshinoService.updateYoshino(userForm);
//
//          logger.info("--- UserYoshinoController.updateStatus END ---");
//          return ResponseEntity.ok().body("Status updated successfully");
//      } catch (Exception e) {
//          logger.error("Error updating status", e);
//          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating status");
//      }
//  }
}
