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
@Table(name = "mst_code")
@Data
@NoArgsConstructor
@NamedEntityGraph(name = "MstCode.withAllAssociations", includeAllAttributes = true)
public class MstCode {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "code_kind", nullable = false)
  private Integer codeKind;

  @Column(name = "code_branch_num", nullable = false)
  private Integer codeBranchNum;

  @Column(name = "code_name", nullable = false)
  private String codeName;

  @Column(name = "status", nullable = false)
  private Integer status;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "updated_mst_user_id", nullable = false)
  private Integer updatedMstUserId;

  @Column(name = "code_bit", nullable = true)
  private Integer codeBit;
}
