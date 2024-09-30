package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MstAcceptingOrderForm {
  private String id;
  private String matterId; // 案件id
  private String estimateId; // 見積id
  private String suppliermanagementId; // 業者仕入先id
  private String suppliermanagementName;// 業者仕入れ先名
  private String orderStatus; // 発注ステータス
  private LocalDateTime conStartDate; // 工事開始日
  private LocalDateTime conEndDate; // 工事終了日
  private String subtotal; // 小計
  private String consumptionTax; // 消費税
  private String total; // 合計
  private String keyLocation; // 鍵の所在
  private Integer registeredUserId; // 登録ユーザーid
  private LocalDateTime registrationDatetime; // 登録日時
  private LocalDateTime lastUpdatedDatetime; // 最終更新日時


  private String cStart;
  private String cEnd;
}
