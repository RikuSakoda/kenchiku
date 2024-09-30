package com.seproject.buildmanager.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "mst_destination")
@Data
public class MstDestination {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;//ID

  @Column(name = "destination_name", nullable = false)
  private String destinationName;//出向先名

  @Column(name = "start_time")
  private LocalDateTime startTime;//開始時間

  @Column(name = "end_time")
  private LocalDateTime endTime;//終了時間

  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;//登録日

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;//更新日

  @Column(name = "updated_mst_user_id")
  private int updatedMstUserId;//更新者ID
  
}
