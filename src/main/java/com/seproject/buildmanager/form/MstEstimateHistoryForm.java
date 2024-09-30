package com.seproject.buildmanager.form;

import lombok.Data;

@Data

public class MstEstimateHistoryForm {

  private Integer id; // 見積履歴ID

  private String estimateId; // 見積ID

  private String updateContent; // 更新内容

  private String estimateSubtotal; // 更新ユーザー

  private String updateUser; // 見積用消費税

  private String updateDatetime; // 見積用合計

}
