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

// 受注発注管理テーブル

@Entity
@Data
@NoArgsConstructor
@Table(name = "mst_acception_order")
public class MstAcceptingOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;// 発注ID

  @Column(name = "matter_id")
  private Integer matterId;// 案件ID

  @Column(name = "estimate_item_id")
  private Integer estimateItemId;// 見積項目ID

  @Column(name = "suppliermanager_id")
  private Integer suppliermanagerId;// 業者・仕入れ先ID

  @Column(name = "order_status")
  private Integer orderStatus;// 発注ステータス

  @Column(name = "construction_start_date")
  private LocalDateTime startDate;// 工事開始日時

  @Column(name = "construction_end_date")
  private LocalDateTime endDate;// 工事終了日時

  @Column(name = "subtotal")
  private Integer subtotal;// 小計

  @Column(name = "consumption_tax")
  private Integer consumptionTax;// 消費税

  @Column(name = "total")
  private Integer total;// 合計

  @Column(name = "key_location")
  private String keyLocation;// 鍵の所在

  @Column(name = "registered_user_id")
  private Integer registeredUserId;// 登録ユーザーID

  @Column(name = "registration_datetime")
  private LocalDateTime registrationDatetime;// 登録日時

  @Column(name = "last_updated_datetime")
  private LocalDateTime lastUpdatedDatetime;// 最終更新日時

}
