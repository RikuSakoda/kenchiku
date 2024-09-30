package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MstAcceptingOrderDetailForm {
  private Integer id;
  private String orderId; // 発注id
  private String matterId; // 案件id
  private String estimateItemId; // 見積id
  private String content; // 内容
  private String manufacturerName; // メーカー名
  private String modelNumber; // 型番
  private String unit; // 単位
  private String unitPrice; // 単価
  private String volume; // 数量
  private String subtotal; // 小計
  private String note; // 備考
  private Integer registeredUserId; // 登録ユーザーid
  private LocalDateTime registrationDatetime; // 登録日時
  private LocalDateTime lastUpdatedDatetime; // 最終更新日時
  private Integer estimateSubtotal; // 見積用小計
  private Integer estimateConsumptionTax; // 見積用消費税
  private Integer estimateTotal; // 見積用合計

}
