package com.seproject.buildmanager.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mst_constoruction")
@Data
@NoArgsConstructor
@NamedEntityGraph(name = "MstConstruction.withAllAssociations", includeAllAttributes = true)
public class MstConstruction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id; // ID

  @Column(name = "cost_group_name")
  private String costGroupName; // 工事区分分類

  @Column(name = "status")
  private Integer status; // ステータス

  @Column(name = "created_at")
  private LocalDateTime createdAt; // 登録日

  @Column(name = "updated_at")
  private LocalDateTime updatedAt; // 更新日

  @Column(name = "updated_mst_user_id")
  private Integer updatedMstUserId; // 更新者ID
}
