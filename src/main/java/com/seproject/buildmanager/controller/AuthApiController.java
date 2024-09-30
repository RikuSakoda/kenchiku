package com.seproject.buildmanager.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.service.MstAuthService;

@RestController
@RequestMapping("api-auth")
public class AuthApiController {

  @Autowired
  private MstAuthService mstAuthService;

  @PostMapping("created")
  public ResponseEntity<String> toggleAuthStatus(@RequestBody Map<String, String> request) {
    String[] requestParams = request.get("name").split(",");
    String save = requestParams[0];
    String select = requestParams[1];

    // ここでnullチェック
    // if文を次の条件で実行
    // ・権限名がnullじゃないかどうか
    // ・権限名の値の長さ1文字以上かどうか
    // trueの場合:正常なので通常の処理を実行
    // falseの場合:エラーなので通常の処理を実行せずに、falseでエラー時の処理を実行
    // エラー時の処理:return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("権限名を入力してください");

    // 権限名のnullチェックおよび長さチェック
    if (save == null || save.length() < 1) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("権限名を入力してください");
    }
    try {
      //
      mstAuthService.saveAuthRegister(save, Integer.parseInt(select));
      return ResponseEntity.ok("Status toggled for user with ID " + save);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling status");
    }
  }

  @PostMapping("auth-update")
  public ResponseEntity<String> toggleAuthUpdateStatus(@RequestBody Map<String, String> request) {
    String[] requestParams = request.get("update").split(",");
    String id = requestParams[0];
    String name = requestParams[1];
    String select = requestParams[2];
    try {

      mstAuthService.saveAuthUpdate(name, Integer.parseInt(select), Integer.parseInt(id));
      return ResponseEntity.ok("Status toggled for user with ID ");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling status");
    }
  }

  @PutMapping("status")
  public ResponseEntity<String> toggleUserStatus(@RequestBody Map<String, Integer> request) {
    Integer authId = request.get("id");
    try {
      mstAuthService.toggleStatus(authId);
      return ResponseEntity.ok("Status toggled for owner with ID " + authId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling status");
    }
  }
}
