package com.seproject.buildmanager.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.entity.MstFacilitiesDetailTitle;
import com.seproject.buildmanager.entity.MstFacilitiesTitle;
import com.seproject.buildmanager.form.MstFacilitiesTitleEnrollmentsForm;
import com.seproject.buildmanager.repository.MstFacilitiesDetailTitleRepositoy;
import com.seproject.buildmanager.repository.MstFacilitiesTitleRepositoy;

@Service
public class MstFacilitiesTitleService {

  private final MstFacilitiesDetailTitleRepositoy mstFacilitiesDetailTitleRepositoy;
  private final MstFacilitiesTitleRepositoy mstFacilitiesTitleRepositoy;

  // コンストラクタ
  public MstFacilitiesTitleService(
      MstFacilitiesDetailTitleRepositoy mstFacilitiesDetailTitleRepositoy,
      MstFacilitiesTitleRepositoy mstFacilitiesTitleRepositoy) {
    this.mstFacilitiesDetailTitleRepositoy = mstFacilitiesDetailTitleRepositoy;
    this.mstFacilitiesTitleRepositoy = mstFacilitiesTitleRepositoy;
  }

  /////////////////////// 取得系メソッド////////////////////////

  public List<MstFacilitiesTitle> getFacilitiesTitles() {
    return this.mstFacilitiesTitleRepositoy.findAll();
  }

  public List<MstFacilitiesDetailTitle> getFacilitiesDetailTitles() {
    return this.mstFacilitiesDetailTitleRepositoy.findAll();
  }

  public List<MstFacilitiesDetailTitle> getFacilitiesDetailTitlesByFacilitiesTitleId(
      Integer titleId) {
    return this.mstFacilitiesDetailTitleRepositoy.findByFacilitiesTitleId(titleId);
  }

  public List<MstFacilitiesTitleEnrollmentsForm> getFacilitiesEnrollmentsForm() {
    List<MstFacilitiesTitleEnrollmentsForm> form =
        new ArrayList<MstFacilitiesTitleEnrollmentsForm>();
    List<MstFacilitiesTitle> facilitiesTitle = this.getFacilitiesTitles();
    for (MstFacilitiesTitle f : facilitiesTitle) {
      List<MstFacilitiesDetailTitle> detailTitle =
          this.getFacilitiesDetailTitlesByFacilitiesTitleId(f.getId());
      MstFacilitiesTitleEnrollmentsForm enrollments = new MstFacilitiesTitleEnrollmentsForm();
      enrollments.setFacilitiesTitle(f);
      enrollments.setFacilitiesDetailTitle(detailTitle);
      form.add(enrollments);
    }
    return form;

  }
}
