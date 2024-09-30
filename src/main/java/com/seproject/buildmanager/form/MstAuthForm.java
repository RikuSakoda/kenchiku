package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MstAuthForm {

  private Integer id; // ID

  private String mstauthtypeid; // 権限ID

  @Size(max = 20, message = "name must be 20 characters or less")
  private String Name; // 権限名

  private String status; // ステータス

  private LocalDateTime createdAt; // 登録日時

  private LocalDateTime updatedAt; // 更新日


  private String createdAt1;
  private String updatedAt1;
}
