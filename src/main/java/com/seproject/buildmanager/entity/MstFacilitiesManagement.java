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
@Table(name = "mst_facilities_management")
@Data
@NoArgsConstructor
public class MstFacilitiesManagement {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "matter_id")
  private Integer matterId;

  @Column(name = "registration_datetime")
  private LocalDateTime registrationDatetime;

  @Column(name = "update_datetime")
  private LocalDateTime updateDatetime;

  @Column(name = "update_user")
  private Integer updateUser;
}
