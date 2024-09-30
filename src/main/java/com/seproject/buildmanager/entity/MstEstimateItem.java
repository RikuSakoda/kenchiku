package com.seproject.buildmanager.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mst_estimate_item")
@Data
@NoArgsConstructor

public class MstEstimateItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id; // ID

  @JoinColumn(name = "estimate_id")
  @OneToOne
  private MstEstimateManagement estimateId; // 見積ID

  @Column(name = "matter_id")
  private Integer matterId; // 案件ID

  @Column(name = "construction_id")
  private Integer constructionId; // 工事区分分類ID

  @Column(name = "construction_name")
  private String constructionName;

  @Column(name = "construction_classification_id")
  private Integer constructionClassificationId; // 工事区分ID

  @Column(name = "construction_classification_name")
  private String constructionClassificationName;

  @Column(name = "unit", updatable = true, nullable = true)
  private Integer unit; // 単位

  @Column(name = "volume")
  private Integer volume; // 数量

  @Column(name = "estimate_unit_price", updatable = true, nullable = true)
  private Integer estimateUnitPrice; // 見積用単価

  @Column(name = "estimate_amount")
  private Integer estimateAmount; // 見積用金額

  @Column(name = "approval_unit_price", updatable = true, nullable = true)
  private Integer approvalUnitPrice; // 原状回復工事費用承諾書用単価

  @Column(name = "tenant_burden_ratio")
  private Integer tenantBurdenRatio; // 入居者負担割合

  @Column(name = "tenant_burden_amount")
  private Integer tenantBurdenAmount; // 入居者負担額

  @Column(name = "tenant_amount")
  private Integer tenantAmount; // 入居者金額

  @Column(name = "note")
  private String note; // 備考

  @Column(name = "registration_datetime")
  private LocalDateTime registrationDatetime; // 登録日時

  @Column(name = "latest_update_datetime")
  private LocalDateTime latestUpdateDatetime; // 最終更新日時

}
