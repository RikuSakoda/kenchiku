package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MstCheckForm {

  private int id; // ID
  private String checkDetail; // グループ名称
  private String status; // ステータス

  private LocalDateTime createdAt; // 登録日
  private LocalDateTime updatedAt; // 最終更新日
  private int updatedMstUserId; // 最終更新者


  private String createdAt1;
  private String updatedAt1;
}
