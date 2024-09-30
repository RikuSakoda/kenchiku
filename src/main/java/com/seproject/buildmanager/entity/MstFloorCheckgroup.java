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
@Table(name = "mst_floor_checkgroup")
@Data
@NoArgsConstructor
public class MstFloorCheckgroup {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "floor_management_id")
  private Integer floorManagementId;

  @Column(name = "floor_plan_name_id")
  private Integer floorPlanNameId;

  @Column(name = "checkgroup_id")
  private String checkgroupId;
}
