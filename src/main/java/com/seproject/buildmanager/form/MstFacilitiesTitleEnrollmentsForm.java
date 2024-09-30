package com.seproject.buildmanager.form;

import java.util.List;
import com.seproject.buildmanager.entity.MstFacilitiesDetailTitle;
import com.seproject.buildmanager.entity.MstFacilitiesTitle;
import lombok.Data;

@Data
public class MstFacilitiesTitleEnrollmentsForm {

  private MstFacilitiesTitle facilitiesTitle;

  private List<MstFacilitiesDetailTitle> facilitiesDetailTitle;
}
