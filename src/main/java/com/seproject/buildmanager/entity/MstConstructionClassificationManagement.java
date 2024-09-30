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

@Entity
@Table(name = "mst_construction_classification_management")
@Data
@NoArgsConstructor
public class MstConstructionClassificationManagement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id; // ID

  @Column(name = "cost_group_id") // 工事区分分類ID
  private Integer costGroupId;

  @Column(name = "group_name") // 工事区分名
  private String groupName;

  @Column(name = "cust_name") // 顧客名
  private String custName;

  @Column(name = "cust_id") // 顧客ID
  private Integer custId;

  @Column(name = "view_detail") // 表示順
  private Integer viewDetail;

  @Column(name = "cost_unit_id") // 単位
  private Integer costUnitId;

  @Column(name = "cost_contents") // 作業内容
  private String costContents;

  @Column(name = "cost_contents_kana") // 作業内容カナ
  private String ContentsKana;

  @Column(name = "cost_price") // 原状回復工事費用承諾書用単価
  private Integer costPrice;

  @Column(name = "cost_price2") // 見積用単価
  private Integer costPrice2;

  @Column(name = "status") // ステータス
  private Integer status;

  @Column(name = "create_at") // 登録日
  private LocalDateTime createAt;

  @Column(name = "updated_at") // 更新日
  private LocalDateTime updatedAt;

  @Column(name = "update_user_id") // 更新者
  private Integer updateUserId;


}
