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
@Table(name = "mst_floor_management")
@Data
@NoArgsConstructor

public class MstFloorManagement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "customer_id")
  private Integer customerId; // 顧客ID

  @Column(name = "customer_name")
  private String customerName; // 顧客名

  @Column(name = "owner_id")
  private Integer ownerId; // オーナーID

  @Column(name = "owner_name")
  private String ownerName; // オーナー名

  @Column(name = "floor_name")
  private String floorName; // 物件名

  @Column(name = "arrangement")
  private String arrangement; // 間取り

  @Column(name = "rent")
  private Integer rent; // 家賃

  @Column(name = "security_deposit")
  private Integer securityDeposit; // 敷金

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

  @Column(name = "mobile_phone")
  private String mobilePhone; // 携帯番号

  @Column(name = "area")
  private float area; // 坪数

  @Column(name = "status")
  private Integer status;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "updated_mst_user_id")
  private Integer updatedMstUserId;

}
