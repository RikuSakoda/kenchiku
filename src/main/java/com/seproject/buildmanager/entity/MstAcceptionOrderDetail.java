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
@Data
@NoArgsConstructor
@Table(name = "mst_acception_order_detail")
public class MstAcceptionOrderDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;// 発注ID

  @Column(name = "order_id")
  private Integer orderId;// 発注ID

  @Column(name = "matter_id")
  private Integer matterId;// 案件ID

  @Column(name = "estimate_id")
  private Integer estimateId;// 見積ID

  @Column(name = "content")
  private String content;// 内容

  @Column(name = "manufacturer_name")
  private String manufacturerName;// メーカー名

  @Column(name = "model_number")
  private String modelNumber;// 型番

  @Column(name = "unit")
  private Integer unit;// 単位

  @Column(name = "unit_price")
  private Integer unitPrice;// 単価

  @Column(name = "volume")
  private Integer volume;// 数量

  @Column(name = "subtotal")
  private Integer subtotal;// 小計

  @Column(name = "note")
  private String note;// 備考

  @Column(name = "registered_user_id")
  private Integer registeredUserId;// 登録ユーザーID

  @Column(name = "registration_datetime")
  private LocalDateTime registrationDatetime;// 登録日時

  @Column(name = "last_updated_datetime")
  private LocalDateTime lastUpdatedDatetime;// 最終更新日時

}
