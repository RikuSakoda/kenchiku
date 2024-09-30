package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class MstFacilitiesManagementForm {
  private Integer id;

  private Integer caseId;

  private String caseName;

  private LocalDateTime registrationDatetime;

  private LocalDateTime updateDatetime;

  private Integer updateUser;

  private List<MstFacilitiesForm> mstFacilitiesForms;
}
