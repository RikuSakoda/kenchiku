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
@Table(name = "mst_suppliermanager")
@Data
@NoArgsConstructor
public class MstSupplierManagement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "classification")
  private Integer classification; // 種別

  @Column(name = "vender_name")
  private String venderName; // 業者・仕入先名

  @Column(name = "post_code")
  private String postCode; // 郵便番号

  @Column(name = "prefectures")
  private Integer prefectures; // 都道府県

  @Column(name = "address")
  private String address; // 住所

  @Column(name = "building_name")
  private String buildingName; // 建物名

  @Column(name = "phone")
  private String phone; // 電話番号

  @Column(name = "fax_num1")
  private String faxNum1; // FAX番号1

  @Column(name = "email")
  private String email; // メールアドレス

  @Column(name = "department")
  private String department; // 担当部署

  @Column(name = "staff_name")
  private String staffName; // 担当者名

  @Column(name = "created_at")
  private LocalDateTime createdAt; // 登録日

  @Column(name = "updated_at")
  private LocalDateTime updatedAt; // 変更日

  @Column(name = "updated_mst_user_id")
  private Integer updatedMstUserId; // ログインユーザーID

  @Column(name = "status") // ステータス
  private Integer status;

}
