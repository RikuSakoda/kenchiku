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
@Table(name = "mst_customer")
@Data
@NoArgsConstructor
public class MstCustomer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id; // 顧客ID

  @Column(name = "cust_kind") // 個人・法人区分
  private Integer custKind;

  @Column(name = "cust_corp_name") // 法人名
  private String corpName;

  @Column(name = "cust_corp_name_kana") // 法人名カナ
  private String corpKana;

  @Column(name = "cust_department_name") // 部署名
  private String department;

  @Column(name = "cust_l_name") // 性
  private String lName;

  @Column(name = "cust_f_name") // 名
  private String fName;

  @Column(name = "cust_l_name_kana") // 性カナ
  private String lNameKana;

  @Column(name = "cust_f_name_kana") // 名カナ
  private String fNameKana;

  @Column(name = "cust_zip") // 郵便番号
  private String zip;

  @Column(name = "cust_prefecture") // 都道府県
  private Integer prefecture;

  @Column(name = "cust_address1") // 住所
  private String address1;

  @Column(name = "cust_address2") // 建物名
  private String address2;

  @Column(name = "cust_tel") // 電話番号
  private String tel;

  @Column(name = "cust_mobile") // 携帯番号
  private String mobile;

  @Column(name = "cust_mail") // メールアドレス
  private String mail;

  @Column(name = "cust_mail_flg") // メールフラグ
  private Integer mailFlg;

  @Column(name = "status") // ステータス
  private Integer status;

  @Column(name = "created_at") // 登録日
  private LocalDateTime createdAt;

  @Column(name = "updated_at") // 更新日
  private LocalDateTime updatedAt;

}
