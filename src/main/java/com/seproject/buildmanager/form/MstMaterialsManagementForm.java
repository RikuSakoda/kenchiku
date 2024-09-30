package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MstMaterialsManagementForm {
  private Integer id;

  private String materialsName;

  private String status;

  private LocalDateTime createdAt;// 表示専用登録日

  private LocalDateTime updatedAt;// 表示専用更新日

  private int updatedMstUserId;

  private String createdAt1;
  private String updatedAt1;

}
