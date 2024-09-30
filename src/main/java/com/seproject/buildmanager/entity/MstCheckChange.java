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
@Table(name = "mst_check_group_history")
@Data
@NoArgsConstructor
public class MstCheckChange {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id; // ID

  @Column(name = "update_id")
  private int updateId; // 対象項目のID

  @Column(name = "status")
  private int status; // ステータス

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;// 更新日


}
