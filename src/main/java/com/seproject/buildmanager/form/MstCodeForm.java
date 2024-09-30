package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MstCodeForm {

  private Integer id;

  private Integer codeKind;

  private Integer codeBranchNum;

  private String codeName;

  private Integer status;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private Integer updatedMstUserId;

  private Integer codeBit;
}
