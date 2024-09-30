package com.seproject.buildmanager.form;

import lombok.Data;

@Data
public class MstCheckItemConstructionClassificationManagementForm {

  private int id; // ID

  private String customerId; // 顧客ID

  private int status; // ステータス

  private String checkId; // チェックID

  private String costId; // コストID

  private String costName; // コスト名


}
