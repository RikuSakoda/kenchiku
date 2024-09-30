package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import lombok.Data;

@Data

public class MstEstimateManagementForm {

  private Integer id; // 見積ID

  private String estimateVersion; // 見積バージョン

  private String acceptionOrderStatus;// 受注ステータス

  private Integer acceptionOrderStatusNum;// 受注ステータス数

  private String estimateSubtotal; // 見積用小計

  private String estimateConsumptionTax; // 見積用消費税

  private String estimateTotal; // 見積用合計

  private String approvalSubtotal; // 原状回復工事費用承諾書小計

  private String approvalConsumptionTax; // 原状回復工事費用承諾書消費税

  private String approvalTotal; // 原状回復工事費用承諾書用合計

  private String memo; // メモ

  private LocalDateTime registrationDatetime; // 登録日時

  private LocalDateTime latestUpdateDatetime; // 最新更新日時

  private MstMatterForm mstMatterForm;



}
