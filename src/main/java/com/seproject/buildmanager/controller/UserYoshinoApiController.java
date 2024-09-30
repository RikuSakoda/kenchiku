package com.seproject.buildmanager.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.service.MstYoshinoService;

@RestController
@RequestMapping("yoshino")
public class UserYoshinoApiController {
  @Autowired
  private MstYoshinoService MstYoshinoservice;

  /**
   * オーナーの有効・無効状態を切り替えるリクエストを処理します。
   * 
   *
   * リクエストボディには、オーナーIDが含まれます。
   * 
   * @param request オーナーIDを含むリクエストボディ
   * @return オーナーのステータス切り替え結果を含むレスポンスエンティティ
   */
  @PutMapping("Status")
  public ResponseEntity<String> toggleUserStatus(@RequestBody Map<String, Integer> request) {
    Integer userId = request.get("userId");
    try {
      MstYoshinoservice.saveStatus(userId);
      return ResponseEntity.ok("Status toggled for owner with ID " + userId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ステータスの変更に失敗しました");
    }
  }

}
