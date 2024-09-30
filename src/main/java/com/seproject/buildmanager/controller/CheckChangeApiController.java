package com.seproject.buildmanager.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.repository.MstCheckChangeRegistrationRepositry;
import com.seproject.buildmanager.service.MstCheckChangeService;

@RestController
@RequestMapping("/api/check-change")
public class CheckChangeApiController {


  @Autowired
  private MstCheckChangeService mstCheckChangeService;

  public CheckChangeApiController(
      MstCheckChangeRegistrationRepositry mstCheckChangeRegistrationRepositry,
      MstCheckChangeService mstCheckChangeService) {
    super();
    this.mstCheckChangeService = mstCheckChangeService;
  }

  @PostMapping("save") // 新規登録
  @Transactional(rollbackFor = Exception.class)
  public ResponseEntity<String> checkGroupSave(@RequestBody Map<String, String> request)
      throws NullPointerException {

    String name = request.get("name");
    name = name.equals("") ? null : name;
    try {


      if (name == null) {
        throw new NullPointerException();
      }

      this.mstCheckChangeService.saveCheckChange(name);
      return ResponseEntity.ok("Status toggled for user with ID " + name);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("グループ名を入力してください");
    }

  }

  @PostMapping("update") // 項目変更
  @Transactional(rollbackFor = Exception.class)
  public ResponseEntity<String> toggleCheckGroupUpdate(@RequestBody Map<String, String> request)
      throws NullPointerException {
    String[] update1 = request.get("update").split(",");
    String update = update1[0];
    String id = update1[1];
    update = update.equals("") ? null : update;
    try {
      if (update == null) {
        throw new NullPointerException();
      }

      this.mstCheckChangeService.updateCheckChange(update, Integer.parseInt(id));
      this.mstCheckChangeService.updateCheckChange(Integer.parseInt(id));
      return ResponseEntity.ok("Status toggled for user with ID " + update);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("グループ名を入力してください");
    }
  }


  @PostMapping("status") // ステータス変更
  @Transactional(rollbackFor = Exception.class)
  public ResponseEntity<String> toggleCheckUpdateStatus(@RequestBody Map<String, String> request) {
    int id = Integer.parseInt(request.get("id"));
    try {
      this.mstCheckChangeService.updateStatusCheckChange(id);
      this.mstCheckChangeService.toggleStatus(id);
      this.mstCheckChangeService.updateCheckChange(id);
      return ResponseEntity.ok("Check group updated with ID " + id);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error updating check group");
    }
  }

}

