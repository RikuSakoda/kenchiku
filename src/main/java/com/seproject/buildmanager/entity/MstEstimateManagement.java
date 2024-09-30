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
@Table(name = "mst_estimate")
@Data
@NoArgsConstructor

public class MstEstimateManagement {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id; // ID

  @Column(name = "estimate_version")
  private Integer estimateVersion; // 見積バージョン

  @Column(name = "accepting_orders_status")
  private Integer acceptionOrderStatus; // 受注ステータス

  @Column(name = "estimate_subtotal")
  private Integer estimateSubtotal; // 見積用小計

  @Column(name = "estimate_consumption_tax")
  private Integer estimateConsumptionTax; // 見積用消費税

  @Column(name = "estimate_total")
  private Integer estimateTotal; // 見積用合計

  @Column(name = "approval_subtotal")
  private Integer approvalSubtotal; // 原状回復工事費用承諾書用消費税

  @Column(name = "approval_consumption_tax")
  private Integer approvalConsumptionTax; // 原状回復工事費用承諾書用消費税

  @Column(name = "approval_total")
  private Integer approvalTotal; // 原状回復工事費用承諾書用合計

  @Column(name = "memo")
  private String memo; // メモ

  @Column(name = "registration_datetime")
  private LocalDateTime registrationDatetime; // 登録日時

  @Column(name = "latest_update_datetime")
  private LocalDateTime latestUpdateDatetime; // 最新更新日時

  @JoinColumn(name = "matter_id")
  @OneToOne
  private MstMatter matter;



}
