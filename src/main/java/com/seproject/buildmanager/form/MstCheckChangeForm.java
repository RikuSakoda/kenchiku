package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MstCheckChangeForm {

  private int id; // ID

  @Size(max = 10, message = "文字数10文字以内で入力してください")
  private String checkGroupName; // グループ名称
  private String status; // ステータス
  private LocalDateTime createdAt; // 登録日
  private LocalDateTime updatedAt; // 更新日
  private int updatedMstUserId; // 更新者

  // 変更履歴
  private int updateId; // 変更された対象項目のID

  // 検索用
  private String createdAt1;
  private String updatedAt1;
}
