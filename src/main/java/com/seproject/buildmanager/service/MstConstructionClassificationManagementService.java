package com.seproject.buildmanager.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.common.ExcelFileService;
import com.seproject.buildmanager.common.MstCodeEnums;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.entity.MstConstruction;
import com.seproject.buildmanager.entity.MstConstructionClassificationManagement;
import com.seproject.buildmanager.form.MstConstructionClassificationManagementtForm;
import com.seproject.buildmanager.repository.MstCodeRepository;
import com.seproject.buildmanager.repository.MstConstructionClassificationManagementRepository;


@Service
public class MstConstructionClassificationManagementService implements
    MstSearchService<MstConstructionClassificationManagementtForm, MstConstructionClassificationManagementtForm> {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstConstructionClassificationManagementRepository mstCostRepository;

  private final MstCodeRepository mstCodeRepository;

  private final MstConstructionService mstConstructionService;

  private final ExcelFileService<MstConstructionClassificationManagement> excelFileService;

  private final CommonService commonService;

  private static final int UNIT = MstCodeEnums.UNIT.getValue();

  private MstConstructionClassificationManagementService(
      MstConstructionClassificationManagementRepository mstCostRepository,
      MstCodeRepository mstCodeRepository, MstConstructionService mstConstructionService,
      ExcelFileService<MstConstructionClassificationManagement> excelFileService,
      CommonService commonService) {
    this.mstCostRepository = mstCostRepository;
    this.mstCodeRepository = mstCodeRepository;
    this.mstConstructionService = mstConstructionService;
    this.excelFileService = excelFileService;
    this.excelFileService.initializeExcel("工事区分管理.xlsx");
    this.commonService = commonService;
  }


  public List<MstConstructionClassificationManagement> getAllCost() { // 全件取得

    logger.info("--- MstCustomerService.getAllCustomers START ---");

    List<MstConstructionClassificationManagement> cost = mstCostRepository.findAll();

    logger.info("--- MstCustomerService.getAllCustomer END ---");

    return cost;

  }

  public List<MstConstructionClassificationManagement> getCustmerName() {// 顧客名取得

    logger.info("--- MstOwnerService.getAllOwner START ---");


    List<MstConstructionClassificationManagement> cost = mstCostRepository.findByCustName();

    logger.info("--- MstUserService.getAllUsers END ---");

    return cost;

  }

  public List<MstConstructionClassificationManagement> getGroupName() {// 分類名取得

    logger.info("--- MstOwnerService.getAllOwner START ---");


    List<MstConstructionClassificationManagement> cost = mstCostRepository.findByGroupName();

    logger.info("--- MstCostService.getGroupName END ---");

    return cost;

  }

  public MstConstructionClassificationManagementtForm registerCostForm() {// インスタンス生成

    logger.info("--- MstCostService.registerCostForm START ---");

    MstConstructionClassificationManagementtForm tmp =
        new MstConstructionClassificationManagementtForm();

    logger.info("--- MstCostService.registerCostForm END ---");

    return tmp;

  }

  public MstConstructionClassificationManagement getCostId(int id) {// id検索
    return mstCostRepository.findById(id).orElse(new MstConstructionClassificationManagement());
  }

  public List<MstConstructionClassificationManagementtForm> viewCostForm() {

    List<MstConstructionClassificationManagement> mstCost = getCustmerName();
    mstCost = getGroupName();
    List<MstConstructionClassificationManagementtForm> mstCostForm =
        new ArrayList<MstConstructionClassificationManagementtForm>();
    for (MstConstructionClassificationManagement cost : mstCost) {
      // MstCustomer cus = mstCustomerService.getCustomerById(owner.getClientId());
      mstCostForm.add(
          updateCostForm(cost, cost.getCustName(), cost.getGroupName()/* cus.getCorpName() */));
    }
    return mstCostForm;
  }

  // 表示用にエンティティからとってきたデータをフォームに格納する
  public MstConstructionClassificationManagementtForm updateCostForm(
      MstConstructionClassificationManagement mstCost, String custName, String groupName) {
    MstConstructionClassificationManagementtForm tmp =
        new MstConstructionClassificationManagementtForm();
    tmp.setCustId(String.valueOf(mstCost.getCustId())); // 顧客ID
    tmp.setCustName(custName); // 顧客名
    tmp.setCostGroupId(String.valueOf(mstCost.getCostGroupId())); // 工事区分分類ID
    tmp.setGroupName(groupName); // 工事区分分類名
    tmp.setViewDetail(mstCost.getViewDetail()); // 表示順
    tmp.setCostContents(mstCost.getCostContents()); // 作業内容
    tmp.setContentsKana(mstCost.getContentsKana()); // 作業内容カナ
    tmp.setCostPrice(mstCost.getCostPrice()); // 原状回復工事費用承諾書用単価
    tmp.setCostPrice2(mstCost.getCostPrice2()); // 見積用単価
    tmp.setCreateAt(mstCost.getCreateAt());
    tmp.setUpdatedAt(mstCost.getUpdatedAt());
    tmp.setId(mstCost.getId());

    // mstCodeRepesitory.findByCodeKindAndBranchNum code_kindとcode_branch_numからデータを検索する
    // 第一引数、code_kind 第二引数、code_branch_num(エンティティに格納されている値)
    // MstCode mstCode =
    // mstCodeRepesitory.findByCodeKindAndBranchNum(MstCodeEnums.INDIVIDUAL_CORPORATE.getValue(),
    // mstOwner.getIndividual()).orElse(new MstCode());
    // tmp.setIndividual(mstCode.getCodeName()); // 取得した値から名前を取得
    MstCode mstCode = mstCodeRepository
        .findByCodeKindAndBranchNum(MstCodeEnums.UNIT.getValue(), mstCost.getCostUnitId())
        .orElse(new MstCode());
    tmp.setCostUnitId(String.valueOf(mstCode.getCodeName()));

    tmp.setStatus(String.valueOf(mstCost.getStatus()));
    return tmp;
  }

  public MstConstructionClassificationManagement getCostClassfinationByContents(String contents,
      Integer groupId) {
    List<MstConstructionClassificationManagement> costList =
        mstCostRepository.findByContents(contents, groupId);
    if (costList == null || costList.isEmpty()) {
      return null;
    }
    return costList.get(0);
  }

  public MstConstructionClassificationManagementtForm CostForm(
      MstConstructionClassificationManagementtForm mstCostForm) {
    MstConstruction a =
        mstConstructionService.getConstructionById(Integer.parseInt(mstCostForm.getCostGroupId()));
    mstCostForm.setGroupName(a.getCostGroupName());
    return mstCostForm;
  }

  // 登録
  public MstConstructionClassificationManagement saveCostRegister(
      MstConstructionClassificationManagementtForm mstCostForm) {

    logger.info("--- MstCostService.saveCost START ---");

    MstConstructionClassificationManagement tmp = new MstConstructionClassificationManagement();
    tmp.setCustId(Integer.parseInt(mstCostForm.getCustId()));
    tmp.setCustName(mstCostForm.getCustName());
    tmp.setCostGroupId(Integer.parseInt(mstCostForm.getCostGroupId()));
    tmp.setGroupName(mstCostForm.getGroupName());
    tmp.setViewDetail(mstCostForm.getViewDetail());
    tmp.setCostContents(mstCostForm.getCostContents());
    tmp.setContentsKana(mstCostForm.getContentsKana());
    tmp.setCostPrice(mstCostForm.getCostPrice());
    tmp.setCostPrice2(mstCostForm.getCostPrice2());


    MstCode mstCode = mstCodeRepository.findByCodeKindAndName(UNIT, mstCostForm.getCostUnitId())
        .orElse(new MstCode());
    tmp.setCostUnitId(mstCode.getCodeBranchNum());

    tmp.setUpdatedAt(LocalDateTime.now());

    try {
      tmp.setStatus(Integer.valueOf(mstCostForm.getStatus()));
    } catch (NumberFormatException e) {
      tmp.setStatus(Constants.STATUS_TRUE);
    }
    tmp.setCreateAt(LocalDateTime.now());
    // tmp.setUpdatedMstUserId();
    tmp.setUpdateUserId(this.commonService.getLoginUserId());

    MstConstructionClassificationManagement result = mstCostRepository.save(tmp);

    logger.info("--- MstOwnerService.saveUser END ---");
    return result;

  }

  // 変更
  public MstConstructionClassificationManagement saveCostUpdate(
      MstConstructionClassificationManagementtForm mstCostForm) {

    logger.info("--- MstCustomerService.saveCustomer START ---");

    MstConstructionClassificationManagement tmp = new MstConstructionClassificationManagement();
    tmp.setId(mstCostForm.getId());
    tmp.setCustId(Integer.parseInt(mstCostForm.getCustId())); // 顧客ID
    tmp.setCustName(mstCostForm.getCustName());
    tmp.setCostGroupId(Integer.parseInt(mstCostForm.getCostGroupId())); // 工事区分分類ID
    tmp.setGroupName(mstCostForm.getGroupName());
    tmp.setViewDetail(mstCostForm.getViewDetail()); // 表示順
    tmp.setCostContents(mstCostForm.getCostContents()); // 作業内容
    tmp.setContentsKana(mstCostForm.getContentsKana()); // 作業内容カナ
    tmp.setCostPrice(mstCostForm.getCostPrice()); // 原状回復工事費用承諾書用単価
    tmp.setCostPrice2(mstCostForm.getCostPrice2()); // 見積用単価
    tmp.setCreateAt(mstCostForm.getCreateAt());
    tmp.setUpdatedAt(mstCostForm.getUpdatedAt());


    MstCode mstCode = mstCodeRepository.findByCodeKindAndName(UNIT, mstCostForm.getCostUnitId())
        .orElse(new MstCode());
    tmp.setCostUnitId(mstCode.getCodeBranchNum());

    tmp.setUpdatedAt(null);

    try {
      tmp.setStatus(Integer.valueOf(mstCostForm.getStatus()));
    } catch (NumberFormatException e) {
      tmp.setStatus(1);
    }

    tmp.setCreateAt(mstCostForm.getCreateAt());
    tmp.setUpdatedAt(LocalDateTime.now());
    tmp.setUpdateUserId(this.commonService.getLoginUserId());
    MstConstructionClassificationManagement result = mstCostRepository.save(tmp);

    logger.info("--- MstCostService.saveCost END ---");
    return result;
  }

  // 表示用にエンティティからとってきたデータをフォームに格納する
  public MstConstructionClassificationManagementtForm updateCostForm(Integer id) {
    MstConstructionClassificationManagement mstCost = mstCostRepository.findByIdAndGroupName(id)
        .orElse(new MstConstructionClassificationManagement()); // ClientNameを含めたmstOwnerを1行取得
    MstConstructionClassificationManagementtForm tmp =
        new MstConstructionClassificationManagementtForm();

    tmp.setId(mstCost.getId());
    tmp.setCostGroupId(String.valueOf(mstCost.getCostGroupId())); // 工事区分分類ID
    tmp.setGroupName(mstCost.getGroupName());
    tmp.setCustId(String.valueOf(mstCost.getCustId())); // 顧客ID
    tmp.setCustName(mstCost.getCustName());
    tmp.setViewDetail(mstCost.getViewDetail()); // 表示順
    tmp.setCostContents(mstCost.getCostContents()); // 作業内容
    tmp.setContentsKana(mstCost.getContentsKana()); // 作業内容カナ
    tmp.setCostPrice(mstCost.getCostPrice()); // 原状回復工事費用承諾書用単価
    tmp.setCostPrice2(mstCost.getCostPrice2()); // 見積用単価
    tmp.setStatus(String.valueOf(mstCost.getStatus()));
    tmp.setCreateAt(mstCost.getCreateAt());
    tmp.setUpdatedAt(mstCost.getUpdatedAt());
    tmp.setUpdateUserId(mstCost.getUpdateUserId());

    MstCode mstCode = mstCodeRepository
        .findByCodeKindAndBranchNum(MstCodeEnums.UNIT.getValue(), mstCost.getCostUnitId())
        .orElse(new MstCode());
    tmp.setCostUnitId(mstCode.getCodeName());
    return tmp;
  }


  public MstConstructionClassificationManagement CostUpdate(String a, Integer b) {

    logger.info("--- MstCustomerService.saveCustomer START ---");

    MstConstructionClassificationManagement n = getCostId(b);

    MstConstructionClassificationManagement tmp = new MstConstructionClassificationManagement();
    tmp.setId(n.getId());
    tmp.setCustId(n.getCustId()); // 顧客ID
    tmp.setCustName(n.getCustName());
    tmp.setCostGroupId(n.getCostGroupId()); // 工事区分分類ID
    tmp.setGroupName(n.getGroupName());
    tmp.setViewDetail(Integer.parseInt(a)); // 表示順
    tmp.setCostContents(n.getCostContents()); // 作業内容
    tmp.setContentsKana(n.getContentsKana()); // 作業内容カナ
    tmp.setCostPrice(n.getCostPrice()); // 原状回復工事費用承諾書用単価
    tmp.setCostPrice2(n.getCostPrice2());// 見積用単価


    // MstCode mstCode =
    // mstCodeRepository.findByCodeName(n.getCostUnitId().toString()).orElse(new MstCode());
    tmp.setCostUnitId(n.getCostUnitId());

    tmp.setUpdatedAt(null);



    try {
      tmp.setStatus(n.getStatus());
    } catch (NumberFormatException e) {
      tmp.setStatus(1);
    }

    tmp.setCreateAt(n.getCreateAt());
    tmp.setUpdatedAt(LocalDateTime.now());
    MstConstructionClassificationManagement result = mstCostRepository.save(tmp);

    logger.info("--- MstCostService.saveCost END ---");
    return result;
  }

  // 検索
  @Override
  public List<MstConstructionClassificationManagementtForm> search(
      MstConstructionClassificationManagementtForm mstCostForm) {
    String custName = "";
    String groupName = "";
    String costContents = "";
    String costPrice = "";
    String costPrice2 = "";
    String status = "";
    String createAt = "";
    String updatedAt = "";

    MstConstructionClassificationManagementtForm cost = mstCostForm;


    custName += this.nullCheck(cost.getCustName());
    groupName += this.nullCheck(cost.getGroupName());
    costContents += this.nullCheck(cost.getCostContents());
    costPrice += this.nullCheck(cost.getCostPrice1());
    costPrice2 += this.nullCheck(cost.getCostPrice3());

    if (cost.getStatus() == null || cost.getStatus().equals("")) {
      status += "";
    } else {
      if (cost.getStatus().equals("active")) {
        status += "1";
      } else {
        status += "0";
      }
    }
    createAt += this.nullCheck(cost.getCreateAt1());
    updatedAt += this.nullCheck(cost.getUpdatedAt1());

    List<MstConstructionClassificationManagement> a = mstCostRepository.search(custName, groupName,
        costContents, costPrice, costPrice2, status, createAt, updatedAt);
    List<MstConstructionClassificationManagementtForm> mstCostFormList = new ArrayList<>();
    for (MstConstructionClassificationManagement cost1 : a) {
      mstCostFormList.add(updateCostForm(cost1.getId()));
    }
    return mstCostFormList;
  }

  public void download(List<MstConstructionClassificationManagement> cost) {
    try {
      this.excelFileService.exportDataTypeExcel(cost, "cost");
    } catch (IOException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }

  public List<MstConstructionClassificationManagement> changeCostForm(
      List<MstConstructionClassificationManagementtForm> costForm) {
    List<MstConstructionClassificationManagement> cost = new ArrayList<>();
    for (MstConstructionClassificationManagementtForm of : costForm) {
      cost.add(saveCostUpdate(of));
    }
    return cost;
  }

  public void upload() {
    this.excelFileService.setSheetName("cost");
    int num = this.excelFileService.getRowNum();
    for (int i = 1; i <= num; i++) {
      mstCostRepository.save(this.excelFileService
          .importDataToRowTypeExcel(new MstConstructionClassificationManagement(), "cost", i));
    }
  }
}
