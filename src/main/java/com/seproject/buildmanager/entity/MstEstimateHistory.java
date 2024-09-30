package com.seproject.buildmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mst_estate_history")
@Data
@NoArgsConstructor
// @NamedEntityGraph(name = "MstCheck.withAllAssociations", includeAllAttributes = true)
public class MstEstimateHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id; // ID

  @Column(name = "estimate_id")
  private Integer estimateId; // 見積ID

  @Column(name = "update_content")
  private Integer updateContent; // 更新内容

  @Column(name = "update_user")
  private Integer updateUser; // 更新ユーザー

  @Column(name = "update_datetime")
  private Integer updateDatetime; // 更新日時

}
