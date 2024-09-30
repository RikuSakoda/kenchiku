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
@Table(name = "mst_matter")
@Data
@NoArgsConstructor
public class MstMatter {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id; // ID PK

  @Column(name = "matter_situation_status")
  private Integer situationStatus;// 状況ステータス

  @Column(name = "status")
  private Integer status;// 有効無効ステータス

  @Column(name = "registration_user_id")
  private Integer registrationUserId;// 登録ユーザーID FK

  @Column(name = "update_user_id")
  private Integer updateUserId;// 更新ユーザーID FK

  @Column(name = "registration_datetime")
  private LocalDateTime registrationDatetime;// 登録日時

  @Column(name = "update_datetime")
  private LocalDateTime updateDatetime;// 更新日時

  @Column(name = "customer_id")
  private Integer customerId;// 顧客ID FK

  @Column(name = "customer_name")
  private String customerName;// 顧客名

  @Column(name = "property_id")
  private Integer propertyId;// 物件ID FK

  @Column(name = "property_name")
  private String propertyName;// 物件名

  @Column(name = "property_address")
  private String propertyAddress;// 物件住所

  @Column(name = "property_building_name")
  private String propertyBuildingName;// 物件建物名

  @Column(name = "task_substance")
  private Integer taskSubstance;// 種別

  @Column(name = "matter_name")
  private String matterName;// 案件名

  @Column(name = "scheduled_visit_datetime")
  private LocalDateTime scheduledVisitDatetime;// 訪問予定日時

  @Column(name = "visit_id")
  private Integer visitId;// 訪問者ID FK

  @Column(name = "visit_name")
  private String visitName;// 訪問者名

  @Column(name = "security_deposit")
  private Integer securityDeposit;// 敷金

  @Column(name = "rental_contract_date")
  private LocalDateTime rentalContractDate;// 賃貸契約日

  @Column(name = "rental_contract_end_date")
  private LocalDateTime rentalContractEndDate;// 賃貸契約終了日

  @Column(name = "facility")
  private boolean facility;// 設備一覧

  @OneToOne
  @JoinColumn(name = "tenant_id")
  private MstTenant tenant;// 入居者ID FK

  @Column(name = "estimate_final_version")
  private Integer estimatefinalversion;// 見積確定バージョン

}
