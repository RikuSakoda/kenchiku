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
@Table(name = "mst_estimate_item_history")
@Data
@NoArgsConstructor

public class MstEstimateItemHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id; // ID

  @Column(name = "estimate_item_id")
  private Integer estimateItemId; // 見積項目ID

  @Column(name = "update_content")
  private String updateContent; // 更新内容

  @Column(name = "update_user")
  private Integer updateUser; // 更新ユーザー

  @Column(name = "update_datetime")
  private LocalDateTime updateDatetime; // 更新日時
}
