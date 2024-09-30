package com.seproject.buildmanager.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.entity.MstFacilities;
import com.seproject.buildmanager.entity.MstFacilitiesDetail;
import com.seproject.buildmanager.entity.MstFacilitiesDetailTitle;
import com.seproject.buildmanager.entity.MstFacilitiesManagement;
import com.seproject.buildmanager.entity.MstFacilitiesTitle;
import com.seproject.buildmanager.form.MstFacilitiesDetailForm;
import com.seproject.buildmanager.form.MstFacilitiesForm;
import com.seproject.buildmanager.form.MstFacilitiesManagementForm;
import com.seproject.buildmanager.form.MstMatterForm;
import com.seproject.buildmanager.repository.MstFacilitiesDetailRepository;
import com.seproject.buildmanager.repository.MstFacilitiesManagementRepository;
import com.seproject.buildmanager.repository.MstFacilitiesRepository;

@Service
public class MstFacilitiesService {

  private final MstFacilitiesRepository mstFacilitiesRepository;
  private final MstFacilitiesManagementRepository mstFacilitiesManagementRepository;
  private final MstFacilitiesDetailRepository mstFacilitiesDetailRepository;

  private final MstFacilitiesTitleService mstFacilitiesTitleService;
  private final MstMatterService mstCaseService;

  // コンストラクタ
  public MstFacilitiesService(MstFacilitiesRepository mstFacilitiesRepository,
      MstFacilitiesManagementRepository mstFacilitiesManagementRepository,
      MstFacilitiesDetailRepository mstFacilitiesDetailRepository,
      MstFacilitiesTitleService mstFacilitiesTitleService, MstMatterService mstCaseService) {
    this.mstFacilitiesRepository = mstFacilitiesRepository;
    this.mstFacilitiesManagementRepository = mstFacilitiesManagementRepository;
    this.mstFacilitiesDetailRepository = mstFacilitiesDetailRepository;
    this.mstFacilitiesTitleService = mstFacilitiesTitleService;
    this.mstCaseService = mstCaseService;
  }



  /////////////////////// 取得系メソッド////////////////////////



  public List<MstFacilities> getFacilities() {
    return this.mstFacilitiesRepository.findAll();
  }

  public List<MstFacilitiesManagement> getFacilitiesManagements() {
    return this.mstFacilitiesManagementRepository.findAll();
  }

  public List<MstFacilitiesDetail> getFacilitiesDetails() {
    return this.mstFacilitiesDetailRepository.findAll();
  }

  // 指定した設備の設備詳細を一覧取得
  public List<MstFacilitiesDetail> getFacilitiesDetailsByFacilitiesId(Integer facilitiesId) {
    return this.mstFacilitiesDetailRepository.findByFacilitiesId(facilitiesId);
  }

  // 指定した設備管理の設備を一覧取得
  public List<MstFacilities> getFacilitiesByFacilitiesManagementId(Integer managementId) {
    return this.mstFacilitiesRepository.findByFacilitiesManagementId(managementId);
  }

  // 指定した案件の設備管理を取得
  public MstFacilitiesManagement getFacilitiesManagementByMatterId(Integer matterId) {
    return this.mstFacilitiesManagementRepository.findByMatterId(matterId).orElse(null);
  }

  public MstFacilitiesManagementForm getFacilitiesManagementByMatterIdToForm(Integer matterId) {
    MstFacilitiesManagement management = this.getFacilitiesManagementByMatterId(matterId);
    return this.convertFacilitiesManagementToForm(management);
  }


  // 設備管理の情報をFormで一覧取得
  public List<MstFacilitiesManagementForm> getFacilitieManagementsFromForm() {
    List<MstMatterForm> matterForms = this.mstCaseService.viewCaseForm();
    List<MstFacilitiesManagementForm> facilitiesManagementForms =
        new ArrayList<MstFacilitiesManagementForm>();
    for (MstMatterForm caseForm : matterForms) {
      MstFacilitiesManagementForm managementForm =
          this.getFacilitieManagementByMatterIdFromForm(caseForm.getId());
      facilitiesManagementForms.add(managementForm);
    }
    return facilitiesManagementForms;
  }

  // mstFacilitiesManagementを案件idで検索し、Formで取得
  public MstFacilitiesManagementForm getFacilitieManagementByMatterIdFromForm(Integer matterId) {
    MstFacilitiesManagement management =
        this.mstFacilitiesManagementRepository.findByMatterId(matterId).orElse(null);
    MstFacilitiesManagementForm managementForm = new MstFacilitiesManagementForm();
    managementForm.setCaseId(matterId);
    if (management != null) {
      managementForm.setId(management.getId());
    } else {
      managementForm.setId(null);
    }
    managementForm.setCaseName(this.mstCaseService.getCaseById(matterId).getMatterName());
    managementForm
        .setMstFacilitiesForms(this.getFacilitiesByManagementIdFromForm(management.getId()));
    return managementForm;
  }

  // mstFacilitiesManagementのidから紐づいている設備項目をFormで一覧取得
  public List<MstFacilitiesForm> getFacilitiesByManagementIdFromForm(Integer managementId) {
    List<MstFacilitiesTitle> mstFacilitieTitles =
        this.mstFacilitiesTitleService.getFacilitiesTitles();
    List<MstFacilitiesForm> facilitiesForms = new ArrayList<MstFacilitiesForm>();
    for (MstFacilitiesTitle facilitieTitle : mstFacilitieTitles) {
      MstFacilities mstFacilities = this.mstFacilitiesRepository
          .findByFacilitiesManagementIdAndTitleId(facilitieTitle.getId(), managementId)
          .orElse(null);
      facilitiesForms.add(this.convertFacilitieToForm(facilitieTitle, mstFacilities));
    }
    return facilitiesForms;
  }

  public List<MstFacilitiesDetailForm> getFacilitiesDetailByFacilitiesIdFromForm(Integer titleId,
      Integer facilitiesId) {
    List<MstFacilitiesDetailTitle> detailTitles =
        this.mstFacilitiesTitleService.getFacilitiesDetailTitlesByFacilitiesTitleId(titleId);
    List<MstFacilitiesDetailForm> detailForms = new ArrayList<MstFacilitiesDetailForm>();
    for (MstFacilitiesDetailTitle detailTitle : detailTitles) {
      MstFacilitiesDetail facilitiesDetail = this.mstFacilitiesDetailRepository
          .findByFacilitiesIdAndTitleId(facilitiesId, detailTitle.getId()).orElse(null);
      detailForms.add(this.convertFacilitiesDetailToForm(detailTitle, facilitiesDetail));
    }
    return detailForms;
  }



  /////////////////////// 保存・更新メソッド//////////////////////////



  // セーブ
  public MstFacilitiesManagement save(MstFacilitiesManagementForm managementForm) {
    MstFacilitiesManagement management =
        this.convertMstFacilitiesManagementEntityToForm(managementForm);
    this.mstCaseService.updateFacilties(managementForm.getId());
    management = this.mstFacilitiesManagementRepository.save(management);
    for (MstFacilitiesForm facilitieForm : managementForm.getMstFacilitiesForms()) {
      MstFacilities facilities = this.convertFacilitiesToEntity(facilitieForm, management.getId());
      facilities = this.mstFacilitiesRepository.save(facilities);
      for (MstFacilitiesDetailForm detailForm : facilitieForm.getMstFacilitiesDetailForms()) {
        MstFacilitiesDetail detail = this.convertFacilitiesToEntity(detailForm, facilities.getId());
        this.mstFacilitiesDetailRepository.save(detail);
      }
    }
    return management;
  }



  /////////////////////// 変換系メソッド(Entity ⇔ Form)////////////////////////

  // 仮置き Entity ⇒ Form
  // titleとentityからFormに変換
  public MstFacilitiesForm convertFacilitieToForm(MstFacilitiesTitle title, MstFacilities entity) {
    MstFacilitiesForm form = new MstFacilitiesForm();
    Integer id = null;
    form.setTitle(title);
    if (entity != null) {
      id = entity.getId();
      form.setId(entity.getId());
      form.setStatus(entity.getStatus());
    } else {
      id = null;
      form.setId(null);
      form.setStatus(1);
    }

    form.setMstFacilitiesDetailForms(
        this.getFacilitiesDetailByFacilitiesIdFromForm(title.getId(), id));
    return form;
  }

  public MstFacilitiesManagementForm convertFacilitiesManagementToForm(
      MstFacilitiesManagement entity) {
    return new MstFacilitiesManagementForm();
  }

  public MstFacilitiesDetailForm convertFacilitiesDetailToForm(MstFacilitiesDetailTitle title,
      MstFacilitiesDetail entity) {
    MstFacilitiesDetailForm detailForm = new MstFacilitiesDetailForm();
    detailForm.setTitle(title);
    if (entity != null) {
      detailForm.setId(entity.getId());
      detailForm.setValue(entity.getValue());
    } else {
      detailForm.setId(null);
      detailForm.setValue(null);
    }
    return detailForm;
  }

  // Form ⇒ Entity
  public MstFacilitiesManagement convertFacilitiesManagementToEntity(
      MstFacilitiesManagementForm form) {
    MstFacilitiesManagement entity = new MstFacilitiesManagement();
    entity.setId(form.getId());
    entity.setMatterId(form.getCaseId());
    entity.setRegistrationDatetime(form.getRegistrationDatetime());
    entity.setUpdateDatetime(form.getUpdateDatetime());
    entity.setUpdateUser(form.getUpdateUser());
    return entity;
  }

  public MstFacilities convertFacilitiesToEntity(MstFacilitiesForm form, Integer managementId) {
    MstFacilities entity = new MstFacilities();
    entity.setId(form.getId());
    entity.setFacilitiesManagementId(managementId);
    entity.setFacilitiesTitle(form.getTitle());
    entity.setStatus(form.getStatus());
    return entity;
  }

  public MstFacilitiesDetail convertFacilitiesToEntity(MstFacilitiesDetailForm form,
      Integer facilitiesId) {
    MstFacilitiesDetail entity = new MstFacilitiesDetail();
    entity.setId(form.getId());
    entity.setFacilitiesId(facilitiesId);
    entity.setFacilitiesDetailTitle(form.getTitle());
    entity.setValue(form.getValue());
    entity.setStatus(1);
    return new MstFacilitiesDetail();
  }

  public MstFacilitiesManagement convertMstFacilitiesManagementEntityToForm(
      MstFacilitiesManagementForm form) {
    MstFacilitiesManagement entity = new MstFacilitiesManagement();
    entity.setId(form.getId());
    entity.setMatterId(form.getCaseId());
    entity.setRegistrationDatetime(form.getRegistrationDatetime());
    entity.setUpdateDatetime(form.getUpdateDatetime());
    entity.setUpdateUser(form.getUpdateUser());
    return entity;
  }
}
