package com.seproject.buildmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mst_facilities")
@Data
@NoArgsConstructor
public class MstFacilities {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "facilities_management_id")
  private Integer facilitiesManagementId;

  @OneToOne
  @JoinColumn(name = "facilities_title_id")
  private MstFacilitiesTitle facilitiesTitle;

  @Column(name = "status")
  private Integer status;
}
