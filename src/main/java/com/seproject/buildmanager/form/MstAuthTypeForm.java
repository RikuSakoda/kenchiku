package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MstAuthTypeForm {

  private Integer id; // ID

  private String authName; // 権限名

  private String status; // ステータス

  private LocalDateTime createdAt; // 登録日時

  private LocalDateTime updatedAt; // 更新日

  private int updatedMstUserId; //

  private String createdAt1;
  private String updatedAt1;

}
