package com.seproject.buildmanager.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

// 受注発注詳細履歴テーブル

@Entity
@Data
@NoArgsConstructor
@Table(name = "mst_acception_order_detail_history")
public class MstAcceptingOrderDetailHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "order_detail_id")
  private Integer orderDetailId; // 発注詳細id

  @Column(name = "note")
  private String note; // 内容

  @Column(name = "registered_user_id")
  private Integer registeredUserId; // 登録ユーザーid

  @Column(name = "registration_datetime")
  private LocalDateTime registrationDatetime; // 登録日

  @Column(name = "last_updated_datetime")
  private LocalDateTime lastUpdatedDatetime; // 最終更新日時

}
