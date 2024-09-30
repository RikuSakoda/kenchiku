package com.seproject.buildmanager.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.repository.MstUserRepository;

/**
 * ユーザ画面のAjaxリクエストを処理するためのコントローラクラスです。
 * このクラスは、ユーザの有効・無効状態の切り替えなど、ユーザに関する操作を行います。
 * 
 * <p>変更履歴：
 * <ul>
 * <li>2024/10/31 - 初版作成</li>
 * </ul>
 * 
 * @since 1.0
 * @version 1.0
 */
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @Autowired
    private MstUserRepository mstUserRepository;

    /**
     * ユーザの有効・無効状態を切り替えるリクエストを処理します。
     * 
     * <p>リクエストボディには、ユーザIDが含まれます。
     * 
     * @param request ユーザIDを含むリクエストボディ
     * @return ユーザのステータス切り替え結果を含むレスポンスエンティティ
     */
    @PutMapping("/status")
    public ResponseEntity<String> toggleUserStatus(@RequestBody Map<String, Integer> request) {
        Integer userId = request.get("userId");
        try {
            mstUserRepository.toggleStatus(userId);
            return ResponseEntity.ok("Status toggled for user with ID " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling status");
        }
    }
}
