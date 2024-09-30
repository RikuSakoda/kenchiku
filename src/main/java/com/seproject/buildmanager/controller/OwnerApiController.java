package com.seproject.buildmanager.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.service.MstOwnerManagementService;

@RestController
@RequestMapping("api-MstOwnerServices")
public class OwnerApiController {

  private final MstOwnerManagementService mstOwnerService;

  public OwnerApiController(MstOwnerManagementService mstOwnerService) {
    super();
    this.mstOwnerService = mstOwnerService;
  }

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
    Integer OwnerId = request.get("ownerId");
    try {
      mstOwnerService.saveStatus(OwnerId);
      return ResponseEntity.ok("Status toggled for owner with ID " + OwnerId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ステータスの変更に失敗しました");
    }
  }

  @PostMapping("upload")
  public ResponseEntity<String> uploadFile() {
    try {
      this.mstOwnerService.upload();
      return ResponseEntity.ok("Status toggled for owner with ID " + "1");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("アップロードに失敗しました");
    }
  }

}
