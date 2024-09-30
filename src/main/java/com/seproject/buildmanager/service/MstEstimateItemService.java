package com.seproject.buildmanager.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.MstCodeEnums;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.entity.MstConstruction;
import com.seproject.buildmanager.entity.MstConstructionClassificationManagement;
import com.seproject.buildmanager.entity.MstEstimateItem;
import com.seproject.buildmanager.entity.MstEstimateManagement;
import com.seproject.buildmanager.entity.MstMatter;
import com.seproject.buildmanager.form.MstEstimateItemForm;
import com.seproject.buildmanager.form.MstEstimateManagementForm;
import com.seproject.buildmanager.repository.MstCodeRepository;
import com.seproject.buildmanager.repository.MstEstimateItemRepository;
import com.seproject.buildmanager.repository.MstEstimateManagementRepository;

@Service
public class MstEstimateItemService {

  private final MstEstimateItemRepository mstEstimateItemRepository;

  private final MstEstimateManagementService mstEstimateManagementService;

  private final MstCodeRepository mstCodeRepesitory;

  private final MstMatterService mstMatterService;

  private final MstCodeService mstCodeService;

  private final MstConstructionService mstConstructionService;

  private final MstConstructionClassificationManagementService mstConstructionClassificationManagementService;

  private final MstEstimateManagementRepository mstEstimateManagementRepository;

  private static final int UNIT = MstCodeEnums.UNIT.getValue();

  private static final int ACCEPTING_ORDERS_STATUS =
      MstCodeEnums.ACCEPTING_ORDERS_STATUS.getValue();

  private static final int SITUATION_STATUS = MstCodeEnums.SITUATION_STATUS.getValue();

  private final Integer InteinitialVersion = 1; // 初期バージョン (1が入ります)

  public MstEstimateItemService(MstEstimateItemRepository mstEstimateItemRepository,
      MstEstimateManagementService mstEstimateManagementService,
      MstCodeRepository mstCodeRepesitory, MstMatterService mstMatterService,
      MstCodeService mstCodeService,
      MstEstimateManagementRepository mstEstimateManagementRepository,
      MstConstructionService mstConstructionService,
      MstConstructionClassificationManagementService mstConstructionClassificationManagementService) {
    this.mstEstimateItemRepository = mstEstimateItemRepository;
    this.mstEstimateManagementService = mstEstimateManagementService;
    this.mstCodeRepesitory = mstCodeRepesitory;
    this.mstMatterService = mstMatterService;
    this.mstCodeService = mstCodeService;
    this.mstEstimateManagementRepository = mstEstimateManagementRepository;
    this.mstConstructionService = mstConstructionService;
    this.mstConstructionClassificationManagementService =
        mstConstructionClassificationManagementService;
  }

  public List<MstEstimateItem> findAll() {
    return mstEstimateItemRepository.getEstimateItem();
  }


  public List<MstEstimateItem> findAllByVerId(Integer id, Integer ver) {
    return mstEstimateItemRepository.getAllEstimateItemVerId(id, ver);
  }

  public List<MstEstimateItem> findAllById(Integer id) {
    return mstEstimateItemRepository.getEstimateItemById(id);
  }



  public MstEstimateItem registForm() {
    MstEstimateItem form = new MstEstimateItem();
    return form;
  }

  // public List<MstEstimateItemForm> findByDisply() {
  //
  // List<MstEstimateItem> mstEstimateItem = findAll();
  // List<MstEstimateItemForm> mstEstimateItemForm = new ArrayList<MstEstimateItemForm>();
  // for (MstEstimateItem EstimateItem : mstEstimateItem) {
  // mstEstimateItemForm.add(updateEstimateItemForm(EstimateItem));
  // }
  // return mstEstimateItemForm;
  // }


  public MstEstimateItemForm findByDisplyById(Integer id, Integer ver) {

    List<MstEstimateItem> mstEstimateItem = findAllByVerId(id, ver);

    return updateEstimateItemForm(mstEstimateItem.get(0));
  }

  public MstEstimateItemForm updateEstimateItemForm(MstEstimateItem mstEstimateItem) {
    MstEstimateItemForm tmp = new MstEstimateItemForm();

    tmp.setId(mstEstimateItem.getId());
    tmp.setMatterId(mstEstimateItem.getMatterId());
    tmp.setConstoructionId(String.valueOf(mstEstimateItem.getConstructionId()));
    tmp.setConstructionName(mstEstimateItem.getConstructionName());
    tmp.setConstoructionClassificationId(
        String.valueOf(mstEstimateItem.getConstructionClassificationId()));
    tmp.setConstructionClassificationName(mstEstimateItem.getConstructionClassificationName());
    MstCode mstCode = mstCodeRepesitory.findByCodeKindAndBranchNum(UNIT, mstEstimateItem.getUnit())
        .orElse(new MstCode());
    tmp.setUnit(mstCode.getCodeName());
    tmp.setVolume(mstEstimateItem.getVolume().toString());
    tmp.setEstimateUnitPrice(mstEstimateItem.getEstimateUnitPrice().toString());
    tmp.setEstimateAmount(mstEstimateItem.getEstimateAmount().toString());
    tmp.setApprovalUnitPrice(mstEstimateItem.getApprovalUnitPrice().toString());
    tmp.setTenantBurdenRatio(mstEstimateItem.getTenantBurdenRatio().toString());
    tmp.setTenantBurdenAmount(mstEstimateItem.getTenantBurdenAmount().toString());
    tmp.setTenantAmount(mstEstimateItem.getTenantAmount().toString());
    tmp.setNote(mstEstimateItem.getNote());

    tmp.setEstimateId(mstEstimateManagementService.updateCaseForm(mstEstimateItem.getEstimateId()));

    return tmp;
  }

  public MstEstimateItem convertStringToEntity(String[] str, Integer matterId)
      throws NullPointerException, NumberFormatException {

    MstEstimateItem mstEstimateItem = new MstEstimateItem();
    mstEstimateItem.setId(null);
    MstConstruction cost = mstConstructionService.getConstructionByName(str[0]);
    if (cost != null) {
      mstEstimateItem.setConstructionId(cost.getId());
      mstEstimateItem.setConstructionName(cost.getCostGroupName());
    } else {
      mstEstimateItem.setConstructionId(0);
      mstEstimateItem.setConstructionName(str[0]);
    }
    Integer costId = 0;
    if (cost != null) {
      costId = cost.getId();
    }
    MstConstructionClassificationManagement mstConstructionClassfication =
        mstConstructionClassificationManagementService.getCostClassfinationByContents(str[1],
            costId);
    if (mstConstructionClassfication != null) {
      mstEstimateItem.setConstructionClassificationId(mstConstructionClassfication.getId());
      mstEstimateItem
          .setConstructionClassificationName(mstConstructionClassfication.getCostContents());
    } else {
      mstEstimateItem.setConstructionClassificationId(0);
      mstEstimateItem.setConstructionClassificationName(str[1]);
    }

    mstEstimateItem.setUnit(Integer.parseInt(str[2]));
    mstEstimateItem.setVolume(Integer.parseInt(str[3]));
    mstEstimateItem.setEstimateUnitPrice(Integer.parseInt(str[4]));
    mstEstimateItem.setEstimateAmount(Integer.parseInt(str[5]));

    MstMatter matter = this.mstMatterService.findDisplayById(matterId);
    MstCode code = this.mstCodeService.getCodeByKindAndName("修繕", SITUATION_STATUS);
    if (matter.getSituationStatus() != code.getCodeBranchNum()) {
      try {
        mstEstimateItem.setApprovalUnitPrice(Integer.parseInt(str[6]));
      } catch (NumberFormatException | NullPointerException e) {
        mstEstimateItem.setApprovalUnitPrice(0);
      }
      mstEstimateItem.setTenantBurdenRatio(Integer.parseInt(str[7]));

      mstEstimateItem.setTenantBurdenAmount(Integer.parseInt(str[8]));
      mstEstimateItem.setTenantAmount(Integer.parseInt(str[9]));
      mstEstimateItem.setNote(str[10]);
    } else {
      mstEstimateItem.setApprovalUnitPrice(0);
      mstEstimateItem.setTenantBurdenRatio(0);

      mstEstimateItem.setTenantBurdenAmount(0);
      mstEstimateItem.setTenantAmount(0);
      mstEstimateItem.setNote(str[6]);
    }

    mstEstimateItem.setMatterId(matterId);

    mstEstimateItem.setRegistrationDatetime(LocalDateTime.now());
    mstEstimateItem.setLatestUpdateDatetime(LocalDateTime.now());
    return mstEstimateItem;
  }

  public void save(MstEstimateItem item) {
    this.mstEstimateItemRepository.save(item);
  }

  public void saveAll(List<MstEstimateItem> itemList) {
    this.mstEstimateItemRepository.saveAll(itemList);
  }

  public MstEstimateItem findDisplayById(Integer id) {
    return mstEstimateItemRepository.findById(id).orElse(new MstEstimateItem());
  }


  public MstEstimateItem saveCopy(MstEstimateItemForm mstEstimateItemForm, MstMatter matter) {

    MstEstimateItem tmp = this.mstEstimateItemFormToEntity(mstEstimateItemForm);


    tmp.setId(null);
    tmp.getEstimateId().setId(null);
    tmp.getEstimateId().setMatter(matter);
    MstEstimateManagement result = mstEstimateManagementRepository.save(tmp.getEstimateId());
    tmp.setEstimateId(result);
    tmp.setMatterId(matter.getId());
    MstEstimateItem resultItem = mstEstimateItemRepository.save(tmp);

    return resultItem;
  }

  public MstEstimateItem mstEstimateItemFormToEntity(MstEstimateItemForm mstEstimateItem) {
    MstEstimateItem tmp = new MstEstimateItem();
    if (mstEstimateItem.getId() != null) {
      tmp.setId(mstEstimateItem.getId());
      tmp.setRegistrationDatetime(LocalDateTime.now());
    }
    tmp.setMatterId(mstEstimateItem.getMatterId());
    tmp.setConstructionId(Integer.parseInt(mstEstimateItem.getConstoructionId()));
    tmp.setConstructionClassificationId(
        Integer.parseInt(mstEstimateItem.getConstoructionClassificationId()));

    MstCode mstCode = this.mstCodeService.getCodeByKindAndName(mstEstimateItem.getUnit(), UNIT);
    tmp.setUnit(mstCode.getCodeBranchNum());
    tmp.setVolume(Integer.parseInt(mstEstimateItem.getVolume()));
    tmp.setEstimateUnitPrice(Integer.parseInt(mstEstimateItem.getEstimateUnitPrice()));
    tmp.setEstimateAmount(Integer.parseInt(mstEstimateItem.getEstimateAmount()));
    tmp.setApprovalUnitPrice(Integer.parseInt(mstEstimateItem.getApprovalUnitPrice()));
    tmp.setTenantBurdenRatio(Integer.parseInt(mstEstimateItem.getTenantBurdenRatio()));
    tmp.setTenantAmount(Integer.parseInt(mstEstimateItem.getTenantAmount()));
    tmp.setNote(mstEstimateItem.getNote());
    tmp.setTenantBurdenAmount(Integer.parseInt(mstEstimateItem.getTenantBurdenAmount()));
    tmp.setConstructionName(mstEstimateItem.getConstructionName());
    tmp.setConstructionClassificationName(mstEstimateItem.getConstructionClassificationName());
    tmp.setEstimateId(this.mstEstimateManagementFormToEntity(mstEstimateItem.getEstimateId()));

    return tmp;
  }

  public MstEstimateManagement mstEstimateManagementFormToEntity(
      MstEstimateManagementForm mstEstimateManagementForm) {
    MstEstimateManagement tmp = new MstEstimateManagement();
    tmp.setId(mstEstimateManagementForm.getId());
    tmp.setEstimateVersion(InteinitialVersion);
    MstCode mstCode = this.mstCodeService.getCodeByKindAndName(
        mstEstimateManagementForm.getAcceptionOrderStatus(), ACCEPTING_ORDERS_STATUS);
    tmp.setAcceptionOrderStatus(mstCode.getCodeBranchNum());
    tmp.setEstimateSubtotal(Integer.parseInt(mstEstimateManagementForm.getEstimateSubtotal()));
    tmp.setApprovalConsumptionTax(
        Integer.parseInt(mstEstimateManagementForm.getApprovalConsumptionTax()));
    tmp.setEstimateConsumptionTax(
        Integer.parseInt(mstEstimateManagementForm.getEstimateConsumptionTax()));
    tmp.setApprovalConsumptionTax(
        Integer.parseInt(mstEstimateManagementForm.getApprovalConsumptionTax()));
    tmp.setEstimateTotal(Integer.parseInt(mstEstimateManagementForm.getEstimateTotal()));
    tmp.setApprovalSubtotal(Integer.parseInt(mstEstimateManagementForm.getApprovalSubtotal()));
    tmp.setApprovalTotal(Integer.parseInt(mstEstimateManagementForm.getApprovalTotal()));
    tmp.setMemo(mstEstimateManagementForm.getMemo());
    tmp.setRegistrationDatetime(LocalDateTime.now());
    tmp.setLatestUpdateDatetime(LocalDateTime.now());


    return tmp;



  }

  public MstEstimateItemForm showMstEstimateItemForm() {
    MstEstimateItemForm tmp = new MstEstimateItemForm();
    return tmp;
  }


}
