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
@Table(name = "mst_estate")
@Data
@NoArgsConstructor
// @NamedEntityGraph(name = "MstCheck.withAllAssociations", includeAllAttributes = true)
public class MstEstate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "pro_id")
  private Integer proId;

  @Column(name = "lay_id")
  private Integer layId;

  @Column(name = "check_id")
  private String checkId;



}
