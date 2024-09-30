package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MstDestinationForm {

  @NotEmpty(message = "出向先名を入力してください")
  @Size(max = 255, message = "255文字以内で入力してください")
  private String destinationName;//出向先名

  @NotEmpty(message = "出向先名を入力してください")
  private LocalDateTime startTime;//開始時間

  @NotEmpty(message = "出向先名を入力してください")
  private LocalDateTime endTime;//終了時間

  @Transient
  private String strStartTime;//開始時間

  @Transient
  private String strEndTime;//終了時間
  
  private Integer id;//ID
  
}
