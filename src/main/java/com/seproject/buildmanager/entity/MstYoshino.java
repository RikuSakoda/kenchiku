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
@Table(name = "mst_yoshino")
@Data
@NoArgsConstructor
// @NamedEntityGraph(name = "MstOwner.withAllAssociations", includeAllAttributes = true)
public class MstYoshino {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;// id

  @Column(name = "cust_kind")
  private Integer custkind;// 顧客id
  
  @Column(name = "cust_corp_name")
  private String custcorpname;// 顧客名

  @Column(name = "cust_corp_name_kana")
  private String custcorpnamekana;// 顧客名かな

  @Column(name = "cust_department_name")
  private String custdepartmentname;// 部署

  @Column(name = "cust_l_name")
  private String custlname;// 姓

  @Column(name = "cust_f_name")
  private String custfname;// 名

  @Column(name = "cust_l_name_kana")
  private String custlnamekana;// 姓かな

  @Column(name = "cust_f_name_kana")
  private String custfnamekana;// 名かな

  @Column(name = "cust_phone")
  private String custphone;// 電話番号

  @Column(name = "cust_mobile_phone")
  private String custmobilephone;// 携帯電話番号

  @Column(name = "status")
  private Integer status;// ステータス
  
  @Column(name = "created_at")
  private LocalDateTime createdat;// 作成日時

  @Column(name = "updated_at")
  private LocalDateTime updatedat;// 更新日時

  @Column(name = "updated_mst_user_id")
  private int updatedmstuserid;// 更新者ID
}
