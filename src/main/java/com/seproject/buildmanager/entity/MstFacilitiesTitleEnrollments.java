package com.seproject.buildmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

// 設備の項目と詳細の紐づけを担う中間テーブル
@Entity
@Table(name = "mst_facilities_title_enrollments")
@Data
@NoArgsConstructor
public class MstFacilitiesTitleEnrollments {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "facilities_title_id")
  private Integer facilitiesTitle;

  @Column(name = "facilities_title_detail_id")
  private Integer facilitiesDetailTitle;
}
