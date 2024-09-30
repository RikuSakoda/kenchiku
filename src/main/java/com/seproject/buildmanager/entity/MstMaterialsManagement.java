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
@Table(name = "mst_materials_management")
@Data
@NoArgsConstructor
@NamedEntityGraph(name = "Mstmaterials.withAllAssociations", includeAllAttributes = true)
public class MstMaterialsManagement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "materials_name", nullable = false)
  private String materialsName;

  @Column(name = "status")
  private Integer status;

  @Column(name = "createdAt")
  private LocalDateTime createdAt;

  @Column(name = "updatedAt")
  private LocalDateTime updatedAt;

  @Column(name = "updated_mst_user_id")
  private Integer updatedMstUserId;
}
