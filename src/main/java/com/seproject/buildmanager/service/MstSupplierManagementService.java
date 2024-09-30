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
import com.seproject.buildmanager.entity.MstSupplierManagement;
import com.seproject.buildmanager.form.MstSupplierManagementForm;
import com.seproject.buildmanager.repository.MstCodeRepository;
import com.seproject.buildmanager.repository.MstSupplierManagementRepository;

@Service
public class MstSupplierManagementService
    implements MstSearchService<MstSupplierManagementForm, MstSupplierManagementForm> {
  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstSupplierManagementRepository mstSupplierManagementRepository;

  private final MstCodeService mstCodeService;

  private final ExcelFileService<MstSupplierManagement> excelFileService;

  private final CommonService commonService;

  // 同じく業者・個人のcode_kindの値を取得
  private static final int SELECT_SUPPLIER = MstCodeEnums.SELECT_SUPPLIER.getValue();

  // Enumから都道府県のcode_kindの値を取得
  private static final int PREFECTURES = MstCodeEnums.PREFECTURES.getValue();

  private MstSupplierManagementService(
      MstSupplierManagementRepository mstSupplierManagementRepository,
      MstCodeRepository mstCodeRepesitory, MstCodeService mstCodeService,
      ExcelFileService<MstSupplierManagement> excelFileService, CommonService commonService) {
    super();
    this.mstSupplierManagementRepository = mstSupplierManagementRepository;
    this.mstCodeService = mstCodeService;
    this.excelFileService = excelFileService;
    this.excelFileService.initializeExcel("業者仕入れ管理データ.xlsx");
    this.commonService = commonService;
  }

  public List<MstSupplierManagement> getAllSupplierManagement() {// 全権検索

    logger.info("--- MstSupplierManagementService.getAllSupplierManagement START ---");

    List<MstSupplierManagement> supplier = mstSupplierManagementRepository.findAll();

    logger.info("--- MstSupplierManagementService.getAllSupplierManagement END ---");

    return supplier;

  }

  public List<MstSupplierManagementForm> viewSupplierForm() {

    List<MstSupplierManagement> mstSupplier = getAllSupplierManagement();
    List<MstSupplierManagementForm> mstSupplierForm = new ArrayList<MstSupplierManagementForm>();
    for (MstSupplierManagement supplier : mstSupplier) {
      // MstCustomer cus = mstCustomerService.getCustomerById(owner.getClientId());
      mstSupplierForm.add(updateSupplierForm(supplier/* cus.getCorpName() */));
    }
    return mstSupplierForm;
  }

  public MstSupplierManagementForm showSupplierManagementForm() {// インスタンス生成

    logger.info("--- MstUserService.registerOwnerForm START ---");

    MstSupplierManagementForm tmp = new MstSupplierManagementForm();

    logger.info("--- MstUserService.registerOwnerForm END ---");

    return tmp;

  }

  public MstSupplierManagement saveSupplierManagement(
      MstSupplierManagementForm mstSupplierManagementForm) {// 登録

    logger.info("--- MstSupplierManagementService.saveSupplierManagement START ---");

    MstSupplierManagement tmp = new MstSupplierManagement();

    if (mstSupplierManagementForm.getId() != null) {
      tmp.setId(mstSupplierManagementForm.getId());
      tmp.setUpdatedAt(LocalDateTime.now());
      tmp.setCreatedAt(mstSupplierManagementForm.getCreatedAt());
    } else {
      tmp.setCreatedAt(LocalDateTime.now());
      tmp.setUpdatedAt(LocalDateTime.now());
    }
    tmp.setUpdatedMstUserId(commonService.getLoginUserId());
    tmp.setVenderName(mstSupplierManagementForm.getVenderName());
    tmp.setPostCode(mstSupplierManagementForm.getPostCode());
    tmp.setAddress(mstSupplierManagementForm.getAddress());
    tmp.setBuildingName(mstSupplierManagementForm.getBuildingName());
    tmp.setPhone(mstSupplierManagementForm.getPhone());
    tmp.setFaxNum1(mstSupplierManagementForm.getFaxNum1());
    tmp.setEmail(mstSupplierManagementForm.getEmail());
    tmp.setDepartment(mstSupplierManagementForm.getDepartment());
    tmp.setStaffName(mstSupplierManagementForm.getStaffName());

    MstCode mstCode = mstCodeService
        .getCodeByKindAndName(mstSupplierManagementForm.getClassification(), SELECT_SUPPLIER);
    tmp.setClassification(mstCode.getCodeBranchNum());

    mstCode = mstCodeService.getCodeByKindAndName(mstSupplierManagementForm.getPrefectures(),
        PREFECTURES);
    tmp.setPrefectures(mstCode.getCodeBranchNum());



    try {
      tmp.setStatus(Constants.STATUS_TRUE);
    } catch (NumberFormatException e) {
      logger.error("Error status MstSuppilerManagement : " + tmp.toString(), e);
      throw new RuntimeException("ステータスが正常ではありません", e);
    }

    // TODO:登録ユーザは未対応
    MstSupplierManagement result = mstSupplierManagementRepository.save(tmp);

    logger.info("--- MstSupplierManagementService.saveSupplierManagement END ---");
    return result;

  }

  public MstSupplierManagement getSuppliereById(Integer id) {
    return mstSupplierManagementRepository.findById(id).orElse(new MstSupplierManagement());
  }

  // 表示用にエンティティからとってきたデータをフォームに格納する
  public MstSupplierManagementForm updateSupplierForm(MstSupplierManagement mstSupplier) {
    MstSupplierManagementForm tmp = new MstSupplierManagementForm();

    tmp.setId(mstSupplier.getId());
    tmp.setClassification(String.valueOf(mstSupplier.getClassification()));
    tmp.setVenderName(mstSupplier.getVenderName());
    tmp.setPostCode(mstSupplier.getPostCode());
    tmp.setAddress(mstSupplier.getAddress());
    tmp.setBuildingName(mstSupplier.getBuildingName());
    tmp.setPhone(mstSupplier.getPhone());
    tmp.setFaxNum1(mstSupplier.getFaxNum1());
    tmp.setEmail(mstSupplier.getEmail());
    tmp.setDepartment(mstSupplier.getDepartment());
    tmp.setStaffName(mstSupplier.getStaffName());
    tmp.setCreatedAt(mstSupplier.getCreatedAt());
    tmp.setUpdatedAt(mstSupplier.getUpdatedAt());
    tmp.setUpdatedMstUserId(String.valueOf(mstSupplier.getUpdatedMstUserId()));
    tmp.setStatus(String.valueOf(mstSupplier.getStatus()));


    // mstCodeRepesitory.findByCodeKindAndBranchNum code_kindとcode_branch_numからデータを検索する
    // 第一引数、code_kind 第二引数、code_branch_num(エンティティに格納されている値)
    MstCode mstCode = mstCodeService.getCodeByKindAndBranch(MstCodeEnums.PREFECTURES.getValue(),
        mstSupplier.getPrefectures());
    tmp.setPrefectures(mstCode.getCodeName());


    MstCode mstCode2 = mstCodeService.getCodeByKindAndBranch(
        MstCodeEnums.SELECT_SUPPLIER.getValue(), mstSupplier.getClassification());
    tmp.setClassification(mstCode2.getCodeName());
    return tmp;
  }

  @Override
  public List<MstSupplierManagementForm> search(
      MstSupplierManagementForm mstSupplierManagementForm) {
    String classification = "";
    String venderName = "";
    String postCode = "";
    String prefectures = "";
    String address = "";
    String buildingName = "";
    String phone = "";
    String faxNum1 = "";
    String email = "";
    String department = "";
    String staffName = "";
    String status = "";
    String creatdat = "";
    String updatedAt1 = "";
    String updatedMstUserId = "";

    MstSupplierManagementForm supplier = mstSupplierManagementForm;

    if (supplier.getClassification() == null || supplier.getClassification().equals("")) {
      classification += "";
    } else {
      MstCode mstCode =
          mstCodeService.getCodeByKindAndName(supplier.getClassification(), SELECT_SUPPLIER);
      supplier.setClassification(mstCode.getCodeBranchNum().toString());
      classification += supplier.getClassification();
    }
    venderName += this.nullCheck(supplier.getVenderName());
    postCode += this.nullCheck(supplier.getPostCode());
    if (supplier.getPrefectures() == null || supplier.getPrefectures().equals("")) {
      prefectures += "";
    } else {
      MstCode mstCode = mstCodeService.getCodeByKindAndName(supplier.getPrefectures(), PREFECTURES);
      supplier.setPrefectures(mstCode.getCodeBranchNum().toString());
      prefectures += supplier.getPrefectures();
    }
    address += this.nullCheck(supplier.getAddress());
    buildingName += this.nullCheck(supplier.getBuildingName());
    phone += this.nullCheck(supplier.getPhone());
    faxNum1 += this.nullCheck(supplier.getFaxNum1());
    email += this.nullCheck(supplier.getEmail());
    department += this.nullCheck(supplier.getDepartment());
    staffName += this.nullCheck(supplier.getStaffName());
    if (supplier.getStatus() == null || supplier.getStatus().equals("")) {
      status += "";
    } else {
      if (supplier.getStatus().equals("active")) {
        status += "1";
      } else {
        status += "0";
      }
    }
    creatdat += this.nullCheck(supplier.getCreatdat());
    updatedAt1 += this.nullCheck(supplier.getUpdatedAt1());

    List<MstSupplierManagement> a = mstSupplierManagementRepository.search(classification,
        venderName, postCode, prefectures, address, buildingName, phone, faxNum1, email, department,
        staffName, status, creatdat, updatedAt1, updatedMstUserId);
    List<MstSupplierManagementForm> mstSupplierManagementFormList = new ArrayList<>();
    for (MstSupplierManagement supplier1 : a) {
      mstSupplierManagementFormList.add(updateSupplierForm(supplier1));
    }

    return mstSupplierManagementFormList;
  }

  public MstSupplierManagementForm searchForm() {

    MstSupplierManagementForm tmp = new MstSupplierManagementForm();

    return tmp;
  }

  public void download(List<MstSupplierManagement> suppliers) {
    try {
      this.excelFileService.exportDataTypeExcel(suppliers, "suppliers");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void upload() {
    this.excelFileService.setSheetName("suppliers");
    int a = this.excelFileService.getRowNum();
    for (int i = 1; i <= a; i++) {
      mstSupplierManagementRepository.save(this.excelFileService
          .importDataToRowTypeExcel(new MstSupplierManagement(), "suppliers", i));
    }
  }

  public List<MstSupplierManagement> changeSupplierForm(
      List<MstSupplierManagementForm> supplierForm) {
    List<MstSupplierManagement> supplier = new ArrayList<MstSupplierManagement>();
    for (MstSupplierManagementForm of : supplierForm) {
      supplier.add(saveSupplierUpdate(of));
    }
    return supplier;
  }

  public MstSupplierManagement saveSupplierUpdate(
      MstSupplierManagementForm mstSupplierManagementForm) {
    MstSupplierManagement tmp = new MstSupplierManagement();

    tmp.setId(mstSupplierManagementForm.getId());
    tmp.setVenderName(mstSupplierManagementForm.getVenderName());
    tmp.setPostCode(mstSupplierManagementForm.getPostCode());
    tmp.setAddress(mstSupplierManagementForm.getAddress());
    tmp.setBuildingName(mstSupplierManagementForm.getBuildingName());
    tmp.setPhone(mstSupplierManagementForm.getPhone());
    tmp.setFaxNum1(mstSupplierManagementForm.getFaxNum1());
    tmp.setEmail(mstSupplierManagementForm.getEmail());
    tmp.setDepartment(mstSupplierManagementForm.getDepartment());
    tmp.setStaffName(mstSupplierManagementForm.getStaffName());
    tmp.setCreatedAt(mstSupplierManagementForm.getCreatedAt());
    tmp.setUpdatedAt(mstSupplierManagementForm.getUpdatedAt());
    tmp.setStatus(Integer.valueOf(mstSupplierManagementForm.getStatus()));


    // mstCodeRepesitory.findByCodeKindAndBranchNum code_kindとcode_branch_numからデータを検索する
    // 第一引数、code_kind 第二引数、code_branch_num(エンティティに格納されている値)
    MstCode mstCode = mstCodeService
        .getCodeByKindAndName(mstSupplierManagementForm.getPrefectures(), PREFECTURES);
    tmp.setPrefectures(mstCode.getCodeBranchNum());


    MstCode mstCode2 = mstCodeService
        .getCodeByKindAndName(mstSupplierManagementForm.getClassification(), SELECT_SUPPLIER);
    tmp.setClassification((mstCode2.getCodeBranchNum()));
    return tmp;
  }

  public void toggleStatus(Integer supplierId) {
    mstSupplierManagementRepository.toggleStatus(supplierId);
  }

  public void useUploadFile() {
    ExcelFileService<MstSupplierManagement> fileService =
        new ExcelFileService<MstSupplierManagement>("業者仕入れ管理データ.xlsx");

    fileService.setSheetName("sheet1");
    int rowNum = fileService.getRowNum();
    for (int i = 1; i < rowNum + 1; i++) {
      // ownerList.add(fileService.importDataToRowTypeExcel(new MstOwner(), "sheet1", i));
      mstSupplierManagementRepository
          .save(fileService.importDataToRowTypeExcel(new MstSupplierManagement(), "sheet1", i));
    }
    fileService.release();
  }

}
