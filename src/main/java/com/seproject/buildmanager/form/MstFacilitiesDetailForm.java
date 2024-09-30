package com.seproject.buildmanager.form;

import com.seproject.buildmanager.entity.MstFacilitiesDetailTitle;
import lombok.Data;

@Data
public class MstFacilitiesDetailForm {
  private Integer id;

  private String value;

  private MstFacilitiesDetailTitle title;
}
