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
@Table(name = "mst_owner_management")
@Data
@NoArgsConstructor
// @NamedEntityGraph(name = "MstOwner.withAllAssociations", includeAllAttributes = true)
public class MstOwnerManagement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;// id

  @Column(name = "client_id")
  private Integer clientId;// 顧客id

  @Column(name = "customer_name")
  private String clientName;// 顧客名

  @Column(name = "individual")
  private Integer individual;// 個人・法人区分

  @Column(name = "corporation")
  private String corporation;// 法人名

  @Column(name = "corporation_kana")
  private String corporationKana;// 法人名かな

  @Column(name = "department")
  private String department;// 部署

  @Column(name = "l_name")
  private String lName;// 姓

  @Column(name = "f_name")
  private String fName;// 名

  @Column(name = "l_name_kana")
  private String lNameKana;// 姓かな

  @Column(name = "f_name_kana")
  private String fNameKana;// 名かな

  @Column(name = "post_code")
  private String postCode;// 郵便番号

  @Column(name = "prefectures")
  private Integer prefectures;// 都道府県

  @Column(name = "address")
  private String address;// 住所

  @Column(name = "building_name")
  private String buildingName;// 建物名

  @Column(name = "phone")
  private String phone;// 電話番号

  @Column(name = "mobile_phone")
  private String mobilePhone;// 携帯番号

  @Column(name = "email")
  private String email;// メールアドレス

  @Column(name = "status")
  private Integer status;// ステータス

  @Column(name = "created_at")
  private LocalDateTime createdAt;// 登録日

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;// 更新日

  @Column(name = "updated_mst_user_id")
  private Integer updatedMstUserId;// 最終更新者id

}
