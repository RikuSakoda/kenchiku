package com.seproject.buildmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

// 案件管理

@Entity
@Data
@NoArgsConstructor
@Table(name = "mst_tenant")
public class MstTenant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id; // 入居者ID

  @Column(name = "last_name")
  private String lastName; // 入居者姓

  @Column(name = "first_name")
  private String firstName; // 入居者名

  @Column(name = "last_name_kana")
  private String lastNameKana; // 入居者姓カナ

  @Column(name = "first_name_kana")
  private String firstNameKana; // 入居者名カナ

  @Column(name = "area_code")
  private String areaCode; // 入居者市外局番

  @Column(name = "city_code")
  private String cityCode; // 入居者市内局番

  @Column(name = "telephone_number")
  private String telNumber; // 入居者電話番号

  @Column(name = "post_code")
  private String postCode; // 入居者転居先郵便番号

  @Column(name = "prefectures")
  private Integer prefectures; // 入居者転居先都道府県

  @Column(name = "address")
  private String address; // 入居者転居先住所

  @Column(name = "building_name")
  private String buildingName; // 入居者転居先建物名

  @Column(name = "bank_name")
  private String bankName; // 入居者銀行名

  @Column(name = "bank_branch_name")
  private String bankBranchName; // 入居者銀行支店名

  @Column(name = "bank_account_number")
  private String bankAccountNumber; // 入居者銀行口座番号

  @Column(name = "bank_account_name")
  private String bankAccountName; // 入居者銀行口座名義

  @Column(name = "bank_account_name_kana")
  private String bankAccountNameKana; // 入居者銀行口座名義カナ

  @Column(name = "cylinder_number")
  private String cylinderNumber; // シリンダー番号

  @Column(name = "note")
  private String note; // 備考

}
