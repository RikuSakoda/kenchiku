package com.seproject.buildmanager.form;

import java.util.List;
import com.seproject.buildmanager.entity.MstFacilitiesTitle;
import lombok.Data;

@Data
public class MstFacilitiesForm {
  private Integer id;

  private Integer status;

  private MstFacilitiesTitle title;

  private List<MstFacilitiesDetailForm> mstFacilitiesDetailForms;
}
