package com.seproject.buildmanager.entity;

import java.time.LocalDateTime;
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
@Table(name = "mst_facilities_detail_title")
@Data
@NoArgsConstructor
public class MstFacilitiesDetailTitle {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @OneToOne
  @JoinColumn(name = "formatId")
  private MstFormat format_id;

  @Column(name = "status")
  private Integer status;

  @Column(name = "preposition")
  private String preposition;

  @Column(name = "postposition")
  private String postposition;

  @Column(name = "registration_datetime")
  private LocalDateTime registrationDatetime;

  @Column(name = "update_datetime")
  private LocalDateTime updateDatetime;

  @Column(name = "update_user")
  private Integer updateUser;
}
