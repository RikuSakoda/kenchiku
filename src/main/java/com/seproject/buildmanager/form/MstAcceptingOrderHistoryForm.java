package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MstAcceptingOrderHistoryForm {
  private String id;
  private String orderId; // 発注id
  private String note; // 内容
  private String registeredUserId; // 登録ユーザーid
  private LocalDateTime registrationDatetime; // 登録日時
  private LocalDateTime lastUpdatedDatetime; // 最終更新日時

}
