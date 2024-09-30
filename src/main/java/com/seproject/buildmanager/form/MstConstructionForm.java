package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MstConstructionForm {

  private Integer id;

  // 工事区分分類
  @Size(max = 20, message = "cost_group_name must be 20 characters or less")
  private String costGroupName;

  private String status;

  private LocalDateTime createdat;

  private LocalDateTime updatedat;

  private int updatedmstuserid;

  // 検索用
  private String createdAt1;
  private String updatedAt1;

}
