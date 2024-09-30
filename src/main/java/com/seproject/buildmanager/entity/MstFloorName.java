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
@Table(name = "mst_floor_plan_name")
@Data
@NoArgsConstructor

public class MstFloorName {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id; // ID

  @Column(name = "floor_plan_name")
  private String floorPlanName; // 間取り名称名称

  @Column(name = "status")
  private Integer status; // ステータス

  @Column(name = "created_at")
  private LocalDateTime createdAt; // 登録日

  @Column(name = "updated_at")
  private LocalDateTime updatedAt; // 最終更新日

  @Column(name = "updated_mst_user_id")
  private Integer updatedMstUserId; // 最終更新者
}
