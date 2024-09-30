package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MstFloorNameForm {

  private Integer id;

  @Size(max = 20, message = "cost_group_name must be 20 characters or less")
  // private String floor_name;
  private String floorPlanName;

  private String status;

  // private LocalDateTime regist_date;
  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private int updatedMstUserId;

  private String createdAt1;

  private String updatedAt1;
}
