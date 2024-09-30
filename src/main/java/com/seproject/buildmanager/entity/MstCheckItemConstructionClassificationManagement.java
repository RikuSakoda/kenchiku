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
@Table(name = "mst_check_item_construction_classification_management")
@Data
@NoArgsConstructor

public class MstCheckItemConstructionClassificationManagement {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "customer_id")
  private Integer customerId;

  @Column(name = "status")
  private int status;

  @Column(name = "check_id")
  private Integer checkId;

  @Column(name = "construction_classification_id")
  private Integer constructionClassificationId;

  @Column(name = "constoruction_id")
  private Integer constractionId;

  @Column(name = "construction_classification_name")
  private String constructionClassificationName;

}
