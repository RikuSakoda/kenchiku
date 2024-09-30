package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import lombok.Data;

@Data

public class MstEstimateItemHistory {

  private Integer id;

  private String estimateItemId;

  private String updateContent;

  private String updateUser;

  private LocalDateTime updateupdateDatetimedAt;
}
