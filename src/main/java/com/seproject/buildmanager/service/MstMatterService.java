package com.seproject.buildmanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.MstCodeEnums;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.entity.MstFloorManagement;
import com.seproject.buildmanager.entity.MstMatter;
import com.seproject.buildmanager.entity.MstTenant;
import com.seproject.buildmanager.form.MstEstimateItemForm;
import com.seproject.buildmanager.form.MstMatterForm;
import com.seproject.buildmanager.form.MstTenantForm;
import com.seproject.buildmanager.repository.MstCodeRepository;
import com.seproject.buildmanager.repository.MstFloorManagementRepository;
import com.seproject.buildmanager.repository.MstMatterRepository;
import com.seproject.buildmanager.repository.MstTenantRepository;

@Service

public class MstMatterService implements MstSearchService<MstMatterForm, MstMatterForm> {
  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstMatterRepository mstMatterRepository;

  private final MstCodeRepository mstCodeRepesitory;

  private final MstCodeService mstCodeService;

  private final MstFloorManagementRepository mstFloorManagementRepository;

  private final MstTenantRepository mstTenantRepository;

  private final CommonService commonService;


  private final MstAcceptingOrderService mstAcceptingOrderService;


  private final Integer estimatePresence = 1; // 見積もりありの状況ステータス

  private final Integer estimateAbsence = 2; // 見積なしの状況ステータス

  private static List<String> statesList =
      new ArrayList<>(Arrays.asList("現地調査・退去立会後", "見積候補あり", "見積承認待ち", "見積承認却下"));


  public MstMatterService(MstMatterRepository mstMatterRepository,
      MstCodeRepository mstCodeRepesitory,
      MstFloorManagementRepository mstFloorManagementRepository, MstCodeService mstCodeService,
      MstTenantRepository mstTenantRepository, CommonService commonService,
      MstAcceptingOrderService mstAcceptingOrderService) {
    this.mstMatterRepository = mstMatterRepository;
    this.mstCodeRepesitory = mstCodeRepesitory;
    this.mstFloorManagementRepository = mstFloorManagementRepository;
    this.mstCodeService = mstCodeService;
    this.mstTenantRepository = mstTenantRepository;
    this.commonService = commonService;
    this.mstAcceptingOrderService = mstAcceptingOrderService;

  }

  // Enumから種別を取得するためののcode_kindの値を取得
  private static final int TASK_SUBSTANCE = MstCodeEnums.TASK_SUBSTANCE.getValue();
  // 都道府県取得
  private static final int PREFECTURES = MstCodeEnums.PREFECTURES.getValue();
  // 状況ステータス取得
  private static final int SITUATION_STATUS = MstCodeEnums.SITUATION_STATUS.getValue();

  public List<MstMatter> findDisplay() {
    List<MstMatter> CaseInfoList = mstMatterRepository.findDisplay();
    return CaseInfoList;
  }

  public List<MstMatterForm> viewCaseForm() {

    List<MstMatter> mstCase = findDisplay();
    List<MstMatterForm> mstCaseForm = new ArrayList<MstMatterForm>();
    for (MstMatter caseNum : mstCase) {
      mstCaseForm.add(updateCaseForm(caseNum));

    }
    return mstCaseForm;
  }

  public List<MstMatterForm> estimateMatter() {

    List<MstMatter> mstCase = findDisplay();
    List<MstMatterForm> mstCaseForm = new ArrayList<MstMatterForm>();
    for (MstMatter caseNum : mstCase) {
      mstCaseForm.add(updateCaseForm(caseNum));
    }
    List<MstMatterForm> mstForm = new ArrayList<MstMatterForm>();
    for (MstMatterForm caseNum : mstCaseForm) {
      for (int i = 0; i < statesList.size(); i++) {
        if (caseNum.getSituationStatus().equals(statesList.get(i))) {
          mstForm.add(caseNum);
        }
      }
    }
    return mstForm;
  }

  // 表示用にエンティティから取ってきたデータをフォームに格納する
  public MstMatterForm updateCaseForm(MstMatter mstMatter) {
    MstMatterForm tmp = new MstMatterForm();

    tmp.setId(mstMatter.getId());
    tmp.setCustomerId(mstMatter.getCustomerId());
    tmp.setCustomerName(mstMatter.getCustomerName());
    if (mstMatter.isFacility() == true) {
      tmp.setFacility("有");
    } else if (mstMatter.isFacility() == false) {
      tmp.setFacility("無");
    }
    tmp.setMatterName(mstMatter.getMatterName());
    // テナント
    tmp.setPropertyBuildingName(mstMatter.getPropertyBuildingName());
    tmp.setPropertyAddress(mstMatter.getPropertyAddress());
    tmp.setPropertyId(mstMatter.getPropertyId());
    tmp.setPropertyName(mstMatter.getPropertyName());
    tmp.setRegistrationDatetime(mstMatter.getRegistrationDatetime());
    tmp.setRegistrationUserId(mstMatter.getRegistrationUserId());
    tmp.setRentalContractDate(mstMatter.getRentalContractDate());
    tmp.setRentalContractEndDate(mstMatter.getRentalContractEndDate());
    tmp.setScheduledVisitDatetime(mstMatter.getScheduledVisitDatetime());
    tmp.setSecurityDeposit(String.valueOf(mstMatter.getSecurityDeposit()));
    tmp.setStatus(String.valueOf(mstMatter.getStatus()));
    tmp.setUpdateDatetime(mstMatter.getUpdateDatetime());
    tmp.setUpdateUserId(mstMatter.getUpdateUserId());
    tmp.setVisitId(mstMatter.getVisitId());
    tmp.setVisitName(mstMatter.getVisitName());
    tmp.setSelectViewMatterForm(mstMatter.getSituationStatus());

    MstCode mstCode =
        mstCodeRepesitory.findByCodeKindAndBranchNum(TASK_SUBSTANCE, mstMatter.getTaskSubstance())
            .orElse(new MstCode());
    tmp.setTaskSubstance(mstCode.getCodeName());

    MstCode mstCode2 = mstCodeRepesitory
        .findByCodeKindAndBranchNum(SITUATION_STATUS, mstMatter.getSituationStatus())
        .orElse(new MstCode());

    List<MstCode> selectCode =
        mstCodeService.getCodeByCodebitAndBranch(SITUATION_STATUS, mstCode2.getCodeBit());
    tmp.setSituationStatusSelectList(selectCode);

    tmp.setSituationStatus(mstCode2.getCodeName());


    // if (mstAcceptingOrderRepository.findOrderStartDateByMatterId(mstMatter.getId())
    // .getStartDate() != null) {
    // tmp.setConStartDate(mstAcceptingOrderRepository
    // .findOrderStartDateByMatterId(mstMatter.getId()).getStartDate());
    // tmp.setConEndDate(
    // mstAcceptingOrderRepository.findOrderEndDateByMatterId(mstMatter.getId()).getEndDate());
    // }



    tmp.setMstTenantForm(this.mstTenantFromFormToEntity(mstMatter.getTenant()));
    return tmp;

  }

  public MstMatter mstMatterFromFormToEntity(MstMatterForm mstMatter) {
    MstMatter tmp = new MstMatter();
    if (mstMatter.getId() != null) {
      tmp.setId(mstMatter.getId());
      tmp.setRegistrationDatetime(mstMatter.getRegistrationDatetime());
      tmp.setUpdateDatetime(LocalDateTime.now());
    } else {
      tmp.setRegistrationDatetime(LocalDateTime.now());
      tmp.setUpdateDatetime(LocalDateTime.now());
    }
    tmp.setCustomerId(mstMatter.getCustomerId());
    tmp.setCustomerName(mstMatter.getCustomerName());

    try {
      if (mstMatter.getFacility().equals("有")) {
        tmp.setFacility(true);
      } else if (mstMatter.getFacility().equals("無")) {
        tmp.setFacility(false);
      }
    } catch (NullPointerException e) {
      tmp.setFacility(false);
    }

    tmp.setMatterName(mstMatter.getMatterName());
    // テナント
    tmp.setPropertyBuildingName(mstMatter.getPropertyBuildingName());
    tmp.setPropertyAddress(mstMatter.getPropertyAddress());
    tmp.setPropertyId(mstMatter.getPropertyId());
    tmp.setPropertyName(mstMatter.getPropertyName());
    tmp.setRegistrationUserId(mstMatter.getRegistrationUserId());
    tmp.setRentalContractDate(mstMatter.getRentalContractDate());
    tmp.setRentalContractEndDate(mstMatter.getRentalContractEndDate());
    tmp.setScheduledVisitDatetime(mstMatter.getScheduledVisitDatetime());
    try {
      tmp.setSecurityDeposit(Integer.parseInt(mstMatter.getSecurityDeposit()));
    } catch (NumberFormatException e) {
      tmp.setSecurityDeposit(null);
    }

    try {
      tmp.setStatus(Integer.valueOf(mstMatter.getStatus()));
    } catch (NumberFormatException e) {
      tmp.setStatus(1);
    }


    tmp.setUpdateUserId(mstMatter.getUpdateUserId());
    tmp.setVisitId(mstMatter.getVisitId());
    tmp.setVisitName(mstMatter.getVisitName());
    MstCode mstCode = mstCodeRepesitory
        .findByCodeKindAndName(TASK_SUBSTANCE, mstMatter.getTaskSubstance()).orElse(new MstCode());
    tmp.setTaskSubstance(mstCode.getCodeBranchNum());

    MstCode mstCode2 =
        mstCodeRepesitory.findByCodeKindAndName(SITUATION_STATUS, mstMatter.getSituationStatus())
            .orElse(new MstCode());

    tmp.setSituationStatus(mstCode2.getCodeBranchNum());
    if (mstMatter.getSituationStatus() == null) {
      tmp.setSituationStatus(1);
    }

    tmp.setTenant(this.mstTenantFromEntityToForm(mstMatter.getMstTenantForm()));

    return tmp;
  }

  public MstTenantForm mstTenantFromFormToEntity(MstTenant tenant) {
    MstTenantForm tmp = new MstTenantForm();
    tmp.setAddress(tenant.getAddress());
    tmp.setAreaCode(tenant.getAreaCode());
    tmp.setBankAccountName(tenant.getBankAccountName());
    tmp.setBankAccountNameKana(tenant.getBankAccountNameKana());
    tmp.setBankAccountNumber(tenant.getBankAccountNumber());
    tmp.setBankBranchName(tenant.getBankBranchName());
    tmp.setBankName(tenant.getBankName());
    tmp.setBuildingName(tenant.getBuildingName());
    tmp.setCityCode(tenant.getCityCode());
    tmp.setCylinderNumber(tenant.getCylinderNumber());
    tmp.setFirstName(tenant.getFirstName());
    tmp.setFirstNameKana(tenant.getFirstNameKana());
    tmp.setId(tenant.getId());
    tmp.setLastName(tenant.getLastName());
    tmp.setLastNameKana(tenant.getLastNameKana());
    tmp.setNote(tenant.getNote());
    tmp.setPostCode(tenant.getPostCode());

    MstCode mstCode =
        this.mstCodeService.getCodeByKindAndBranch(PREFECTURES, tenant.getPrefectures());
    if (mstCode != null) {
      tmp.setPrefectures(mstCode.getCodeName());
    } else {
      tmp.setPrefectures("");
    }
    tmp.setTelephoneNumber(tenant.getTelNumber());

    return tmp;
  }

  public MstTenant mstTenantFromEntityToForm(MstTenantForm tenant) {
    MstTenant tmp = new MstTenant();
    tmp.setAddress(tenant.getAddress());
    tmp.setAreaCode(tenant.getAreaCode());
    tmp.setBankAccountName(tenant.getBankAccountName());
    tmp.setBankAccountNameKana(tenant.getBankAccountNameKana());
    tmp.setBankAccountNumber(tenant.getBankAccountNumber());
    tmp.setBankBranchName(tenant.getBankBranchName());
    tmp.setBankName(tenant.getBankName());
    tmp.setBuildingName(tenant.getBuildingName());
    tmp.setCityCode(tenant.getCityCode());
    tmp.setCylinderNumber(tenant.getCylinderNumber());
    tmp.setFirstName(tenant.getFirstName());
    tmp.setFirstNameKana(tenant.getFirstNameKana());
    tmp.setId(tenant.getId());
    tmp.setLastName(tenant.getLastName());
    tmp.setLastNameKana(tenant.getLastNameKana());
    tmp.setNote(tenant.getNote());
    tmp.setPostCode(tenant.getPostCode());
    MstCode mstCode =
        this.mstCodeService.getCodeByKindAndName(tenant.getPrefectures(), PREFECTURES);
    if (mstCode != null) {
      tmp.setPrefectures(mstCode.getCodeBranchNum());
    } else {
      tmp.setPrefectures(null);
    }

    tmp.setPrefectures(mstCode.getCodeBranchNum());
    tmp.setTelNumber(tenant.getTelephoneNumber());

    return tmp;
  }

  /**
   * 新規登録用のフォームを作成し返却します。
   * 
   * @return MstCaseForm ユーザフォーム
   */
  public MstMatterForm registerCaseForm() {

    logger.info("--- MstCaseService.registerCaseForm START ---");

    MstMatterForm tmp = new MstMatterForm();
    tmp.setMstTenantForm(new MstTenantForm());

    logger.info("--- MstCaseService.registerCaseForm END ---");

    return tmp;

  }

  public MstMatter findDisplayById(Integer id) {
    return mstMatterRepository.findDisplayById(id).orElse(new MstMatter());
  }

  public MstMatter saveCase(MstMatterForm mstCaseForm) {

    logger.info("--- MstCaseService.saveCase START ---");

    MstMatter tmp = this.mstMatterFromFormToEntity(mstCaseForm);

    tmp.setTenant(this.mstTenantRepository.save(tmp.getTenant()));

    tmp.setRegistrationUserId(this.commonService.getLoginUserId());

    MstMatter result = mstMatterRepository.save(tmp);

    logger.info("--- MstCaseService.saveCase END ---");

    return result;


  }

  public List<MstMatterForm> selectForm() {
    List<MstFloorManagement> floor = mstFloorManagementRepository.findDisplay();
    List<MstMatterForm> mstCaseForm = new ArrayList<MstMatterForm>();
    for (MstFloorManagement floorNum : floor) {
      mstCaseForm.add(floorForm(floorNum));

    }
    return mstCaseForm;
  }

  public MstMatterForm floorForm(MstFloorManagement mstFloorManagement) {
    MstMatterForm tmp = new MstMatterForm();

    tmp.setId(mstFloorManagement.getId());
    tmp.setPropertyName(mstFloorManagement.getFloorName());
    tmp.setPropertyAddress(mstFloorManagement.getPostCode());

    return tmp;
  }

  public MstMatter getCaseById(Integer id) {
    return mstMatterRepository.findById(id).orElse(new MstMatter());
  }

  public MstMatter saveStatus(Integer id) {
    MstMatter mstCase = getCaseById(id);
    mstCase.setUpdateDatetime(LocalDateTime.now());

    MstMatter result = mstMatterRepository.save(mstCase);
    mstMatterRepository.toggleStatus(id);
    return result;
  }

  public MstMatter save(Integer id, String view) {
    MstMatter mstCase = getCaseById(id);
    MstCode mstCode =
        mstCodeRepesitory.findByCodeKindAndName(SITUATION_STATUS, view).orElse(new MstCode());
    mstCase.setSituationStatus(mstCode.getCodeBranchNum());
    mstCase.setUpdateDatetime(LocalDateTime.now());

    MstMatter result = mstMatterRepository.save(mstCase);
    return result;
  }

  public MstMatter save(MstMatter matter) {
    return mstMatterRepository.save(matter);
  }

  public List<MstMatterForm> search(MstMatterForm mstMatterForm) {
    String situationStatus = "";
    String taskSubstance = "";
    String visit = "";
    String matterName = "";
    String customerName = "";
    String propertyName = "";
    String propertyAddress = "";
    String propertyBuildingName = "";
    String facility = "";
    String createdAt1 = "";
    String updatedAt1 = "";

    MstMatterForm matter = mstMatterForm;
    if (matter.getSituationStatus() == null || matter.getSituationStatus().equals("")) {
      situationStatus += "";
    } else {
      MstCode mstCode =
          mstCodeService.getCodeByKindAndName(matter.getSituationStatus(), SITUATION_STATUS);
      matter.setSituationStatus(mstCode.getCodeBranchNum().toString());
      situationStatus += matter.getSituationStatus();
    }
    if (matter.getTaskSubstance() == null || matter.getTaskSubstance().equals("")) {
      taskSubstance += "";
    } else {
      MstCode mstCode =
          mstCodeService.getCodeByKindAndName(matter.getTaskSubstance(), TASK_SUBSTANCE);
      matter.setTaskSubstance(mstCode.getCodeBranchNum().toString());
      taskSubstance += matter.getTaskSubstance();
    }
    visit += nullCheck(matter.getVisit());
    matterName += nullCheck(matter.getMatterName());
    customerName += nullCheck(matter.getCustomerName());
    propertyName += nullCheck(matter.getPropertyName());
    propertyAddress += nullCheck(matter.getPropertyAddress());
    propertyBuildingName += nullCheck(matter.getPropertyBuildingName());
    if (matter.getFacility() == null || matter.getFacility().equals("")) {
      facility += "";
    } else {
      if (matter.getFacility().equals("有")) {
        facility = "1";
      } else {
        facility = "0";
      }
    }
    createdAt1 += nullCheck(matter.getCreatedAt1());
    updatedAt1 += nullCheck(matter.getUpdatedAt1());

    List<MstMatter> a =
        mstMatterRepository.search(situationStatus, taskSubstance, visit, matterName, customerName,
            propertyName, propertyAddress, propertyBuildingName, facility, createdAt1, updatedAt1);

    List<MstMatterForm> mstCaseFormList = new ArrayList<>();
    for (MstMatter case3 : a) {
      mstCaseFormList.add(updateCaseForm(case3));
    }
    return mstCaseFormList;

  }

  public MstMatter saveCopy(MstMatterForm mstCaseForm, Integer ver) {

    logger.info("--- MstCaseService.saveCase START ---");

    MstMatter tmp = this.mstMatterFromFormToEntity(mstCaseForm);

    tmp.setTenant(this.mstTenantRepository.save(tmp.getTenant()));

    tmp.setRegistrationUserId(this.commonService.getLoginUserId());

    tmp.setId(null);

    // ver = null; // テスト

    if (ver != null) {
      tmp.setSituationStatus(estimatePresence); // 見積あり
    } else {
      tmp.setSituationStatus(estimateAbsence); // 見積なし
    }


    MstMatter result = mstMatterRepository.save(tmp);

    logger.info("--- MstCaseService.saveCase END ---");

    return result;


  }

  public void updateFacilties(Integer id) {
    this.mstMatterRepository.updateFicilties(id);
  }

  public List<String> ver(List<MstEstimateItemForm> mst) {
    List<String> string = new ArrayList<String>();
    for (MstEstimateItemForm m : mst) {
      string.add(m.getEstimateId().getEstimateVersion());
    }
    return string;
  }

  public List<MstMatterForm> selectViewMatterForm(List<MstMatterForm> selectView) {
    List<MstMatterForm> form = new ArrayList<MstMatterForm>();
    for (MstMatterForm a : selectView) {
      if (a.getSelectViewMatterForm() >= 6 && a.getSelectViewMatterForm() < 11) {
        a.setConStartDate(mstAcceptingOrderService.getEarliestOrderStartDates(a.getId()));
        a.setConEndDate(mstAcceptingOrderService.getLattermostOrderEndDates(a.getId()));
        form.add(a);
      }
    }
    return form;

  }

}
