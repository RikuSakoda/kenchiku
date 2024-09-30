package com.seproject.buildmanager.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.form.MstAuthTypeForm;
import com.seproject.buildmanager.repository.MstAuthTypeRepository;
import com.seproject.buildmanager.service.MstAuthTypeService;
import com.seproject.buildmanager.validation.ValidationGroups;

@RestController
@RequestMapping("auth-type")
public class AuthTypeApiController {
  @Autowired
  private MstAuthTypeRepository mstAuthTypeRepository;

  @Autowired
  private MstAuthTypeService mstAuthTypeService;

  @PostMapping("register")
  public ResponseEntity<String> toggleAuthTypeStatus(
      @Validated(ValidationGroups.Update.class) MstAuthTypeForm mstAuthTypeForm,
      BindingResult result, @RequestBody Map<String, String> request) {

    String name = request.get("name");
    // ここで入力された文字が空白ではないか確認する。
    if (name.equals("")) {
      name = null;
    }
    try {

      mstAuthTypeService.saveAuthTypeRegister(name);
      return ResponseEntity.ok("Status toggled for user with ID " + name);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("権限を入力してください");
    }
  }

  @PostMapping("update")
  public ResponseEntity<String> toggleAuthTypeUpdateStatus(
      @Validated(ValidationGroups.Update.class) MstAuthTypeForm mstAuthTypeForm,
      BindingResult result, @RequestBody Map<String, String> request) {

    String[] update1 = request.get("update").split(",");
    String update = update1[0];
    String id = update1[1];
    if (update.equals("")) {
      update = null;
    }
    try {

      mstAuthTypeService.saveAuthTypeUpdate(update, Integer.parseInt(id));
      return ResponseEntity.ok("Status toggled for user with ID " + update);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("権限を入力してください");
    }
  }

  // @PostMapping("KojiKubunBunrui") // フォームに入力されたグループ名を受け取ってserviceメソッドを実行して、同じ画面へ遷移する
  // public String saveCost_group_name(@RequestParam("koji_kubun_bunrui_save") String
  // cost_group_name,
  // Model model) {
  // mstConstructionService.saveConstructionRegister(cost_group_name);
  // return "redirect:/koji_kubun_bunrui";
  // }

  @PutMapping("status")
  public ResponseEntity<String> toggleUserStatus(@RequestBody Map<String, Integer> request) {
    Integer AuthTypeId = request.get("id");
    try {
      mstAuthTypeRepository.toggleStatus(AuthTypeId);
      return ResponseEntity.ok("Status toggled for owner with ID " + AuthTypeId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling status");
    }
  }



}
