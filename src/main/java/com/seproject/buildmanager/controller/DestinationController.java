package com.seproject.buildmanager.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.seproject.buildmanager.entity.MstDestination;
import com.seproject.buildmanager.form.MstDestinationForm;
import com.seproject.buildmanager.service.CommonService;
import com.seproject.buildmanager.service.MstDestinationService;
import com.seproject.buildmanager.validation.ValidationGroups;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("destination")
public class DestinationController {

    private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

    private final MstDestinationService mstDestinationService;
    private final CommonService commonService;

    @Autowired
    public DestinationController(MstDestinationService mstDestinationService, CommonService commonService) {
        this.mstDestinationService = mstDestinationService;
        this.commonService = commonService;
    }

    // 一覧表示を行うメソッド
    @GetMapping("")
    public String getAllDestination(
            HttpServletRequest request, 
            Model model,
            @RequestParam(name = "search", required = false) String search) {

        logger.info("--- DestinationController.getAllDestination START ---");

        if (search != null) {
            model.addAttribute("searchParam", search);
        } else {
            model.addAttribute("searchParam", "");
        }

        model.addAttribute("destinations", mstDestinationService.findAll());
        model.addAttribute("statusTrue", Constants.STATUS_TRUE);

        logger.info("--- DestinationController.getAllDestination END ---");
        return "destination/destination";
    }

    // 新規登録画面を表示するメソッド  
    @GetMapping("register")
    public String showDestinationForm(HttpSession session, Model model) {
        logger.info("--- DestinationController.showDestinationForm START ---");

        model.addAttribute("mstDestinationForm", new MstDestinationForm());

        logger.info("--- DestinationController.showDestinationForm END ---");
        return "destination/register";
    }

    // 新規登録の確認画面を表示するメソッド  
    @PostMapping("register")
    public String processRegistration(
            HttpServletRequest request,
            @ModelAttribute("mstDestinationForm") @Validated(ValidationGroups.Registration.class) MstDestinationForm mstDestinationForm,
            BindingResult bindingResult, 
            HttpSession session, 
            Model model) {

        logger.info("--- DestinationController.processRegistration START ---");

        model.addAttribute("mstDestinationForm", mstDestinationForm);

        logger.info("--- DestinationController.processRegistration END ---");
        return "destination/register_confirm";
    }

    // 新規登録を行うメソッド
    @PostMapping("save-register")
    public String saveDestinationRegister(
            MstDestinationForm mstDestinationForm,
            @AuthenticationPrincipal LoginUserDetails user) {

        logger.info("--- DestinationController.saveDestinationRegister START ---");

        mstDestinationService.saveDestinationRegister(mstDestinationForm);

        logger.info("--- DestinationController.saveDestinationRegister END ---");
        return "redirect:/destination/save-register";
    }

    // 新規登録完了画面を表示するメソッド
    @GetMapping("save-register")
    public String createCompleteRegister() {
        return "destination/register_end";
    }
    
    //変更登録画面を表示するメソッド
    @GetMapping("update")
    public String updateDestinationForm(@RequestParam(value = "id") String id, HttpSession session, Model model) {
        logger.info("--- DestinationController.updateDestinationForm START ---");
              
        MstDestination mstDestination = mstDestinationService.getDestinationId(Integer.parseInt(id));
        MstDestinationForm mstDestinationForm = mstDestinationService.updateDestinationForm(mstDestination.getId());

        String formattedStartTime = this.commonService.getDatetimeLocalFormatString(mstDestinationForm.getStartTime());
        String formattedEndTime = this.commonService.getDatetimeLocalFormatString(mstDestinationForm.getEndTime());
        mstDestinationForm.setStrStartTime(formattedStartTime);
        mstDestinationForm.setStrEndTime(formattedEndTime);
        
        model.addAttribute("mstDestinationForm", mstDestinationForm);
        logger.info("--- DestinationController.updateDestinationForm END ---");

        return "destination/update";
    }

    
    //変更登録確認画面を表示するメソッド
    @PostMapping("update")
    public String processUpdate(
         @ModelAttribute("mstDestinationForm") @Validated(ValidationGroups.Update.class) MstDestinationForm mstDestinationForm,
         BindingResult bindingResult, HttpSession session, Model model) {
       logger.info("--- DestinationController.processUpdate START ---");
     
       model.addAttribute("mstDestinationForm", mstDestinationForm);
       logger.info("--- DestinationController.processUpdate END ---");
       return "destination/update_confirm";
    }
    
    //変更登録を行うメソッド
    @PostMapping("save-update")
    public String saveDestinationUpdate(MstDestinationForm mstDestinationForm,
         @AuthenticationPrincipal LoginUserDetails user) {
       logger.info("--- DestinationController.saveDestinationUpdate START ---");

       mstDestinationService.updateDestination(mstDestinationForm);

       logger.info("--- DestinationController.saveDestinationUpdate END ---");
       return "redirect:/destination/save-update";
    }

    //変更登録完了画面を表示するメソッド
    @GetMapping("save-update")
    public String updateComplete() {
       return "destination/update_end";
    }
    
}
