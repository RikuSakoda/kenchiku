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
import com.seproject.buildmanager.entity.MstMaterialsManagement;
import com.seproject.buildmanager.form.MstMaterialsManagementForm;
import com.seproject.buildmanager.repository.MstMaterialsManagementRepository;

@Service
public class MstMaterialsManagementService
    implements MstSearchService<MstMaterialsManagementForm, MstMaterialsManagementForm> {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstMaterialsManagementRepository mstMaterialsRepository;


  private final CommonService commonService;

  private final ExcelFileService<MstMaterialsManagement> fileService;

  public MstMaterialsManagementService(MstMaterialsManagementRepository mstMaterialsRepository,
      ExcelFileService<MstMaterialsManagement> excelFileService, CommonService commonService) {
    super();
    this.mstMaterialsRepository = mstMaterialsRepository;
    this.commonService = commonService;
    this.fileService = excelFileService;
    this.fileService.initializeExcel("資材メーカー管理.xlsx");
  }

  public MstMaterialsManagementForm mstMaterialsForm() {
    MstMaterialsManagementForm mstMaterialsForm = new MstMaterialsManagementForm();
    return mstMaterialsForm;
  }

  public MstMaterialsManagement saveMaterialsRegister(String name) {// 登録

    logger.info("--- MstConstructionService.saveConstructionRegister START ---");



    MstMaterialsManagement tmp = new MstMaterialsManagement();
    tmp.setMaterialsName(name);

    try {
      tmp.setStatus(Constants.STATUS_TRUE);
    } catch (NumberFormatException e) {
      throw new RuntimeException("ステータスが正常ではありません", e);
    }
    tmp.setCreatedAt(LocalDateTime.now());
    tmp.setUpdatedMstUserId(this.commonService.getLoginUserId());
    tmp.setUpdatedAt(LocalDateTime.now());

    MstMaterialsManagement result = mstMaterialsRepository.save(tmp);

    logger.info("--- MstConstructionService.saveConstructionRegister END ---");
    return result;

  }

  public List<MstMaterialsManagement> getAllMaterials() {

    logger.info("--- MstMaterialsManagementService.getAllMaterials START ---");

    List<MstMaterialsManagement> materials = mstMaterialsRepository.findAll();

    logger.info("--- MstMaterialsManagementService.getAllMaterials END ---");

    return materials;
  }

  public MstMaterialsManagement saveMaterialsUpdate(String name, Integer id) {// 変更

    logger.info("--- MstConstructionService.saveUser START ---");
    MstMaterialsManagement a =
        mstMaterialsRepository.findById(id).orElse(new MstMaterialsManagement());
    MstMaterialsManagement tmp = new MstMaterialsManagement();
    tmp.setId(id);
    tmp.setMaterialsName(name);
    try {
      tmp.setStatus(a.getStatus());
    } catch (NumberFormatException e) {
      throw new RuntimeException("ステータスが正常ではありません", e);
    }
    tmp.setCreatedAt(a.getCreatedAt());
    tmp.setUpdatedAt(LocalDateTime.now());
    tmp.setUpdatedMstUserId(this.commonService.getLoginUserId());
    // tmp.setUpdate_user_id(this.commonService.getLoginUserId());// userのidがわからないため保留
    MstMaterialsManagement update = mstMaterialsRepository.save(tmp);

    logger.info("--- MstConstructionService.saveUser END ---");
    return update;

  }

  @Override
  public List<MstMaterialsManagementForm> search(MstMaterialsManagementForm mstMaterialsForm) {
    String materialsName = "";
    String createdAt = "";
    String updatedAt = "";
    String status = "";

    MstMaterialsManagementForm materials = mstMaterialsForm;

    materialsName += nullCheck(materials.getMaterialsName());
    if (materials.getStatus() == null || materials.getStatus().equals("")) {
      status += "";
    } else {
      if (materials.getStatus().equals("active")) {
        status += "1";
      } else {
        status += "0";
      }
    }
    createdAt += nullCheck(materials.getCreatedAt1());
    updatedAt += nullCheck(materials.getUpdatedAt1());



    List<MstMaterialsManagement> a =
        mstMaterialsRepository.search(materialsName, status, createdAt, updatedAt);
    List<MstMaterialsManagementForm> mstMaterialsFormList = new ArrayList<>();
    for (MstMaterialsManagement materials1 : a) {
      mstMaterialsFormList.add(updateMaterialsForm(materials1.getId()));
    }
    return mstMaterialsFormList;
  }

  public MstMaterialsManagementForm updateMaterialsForm(Integer id) {
    MstMaterialsManagement mstMaterials =
        mstMaterialsRepository.findById(id).orElse(new MstMaterialsManagement());
    MstMaterialsManagementForm tmp = new MstMaterialsManagementForm();
    tmp.setId(mstMaterials.getId());
    tmp.setMaterialsName(mstMaterials.getMaterialsName());
    tmp.setStatus(mstMaterials.getStatus().toString());
    tmp.setCreatedAt(mstMaterials.getCreatedAt());
    tmp.setUpdatedAt(mstMaterials.getUpdatedAt());
    tmp.setUpdatedMstUserId(mstMaterials.getUpdatedMstUserId());
    return tmp;
  }

  public MstMaterialsManagement updateMaterials(MstMaterialsManagementForm mmf) {
    MstMaterialsManagement tmp = new MstMaterialsManagement();
    tmp.setId(mmf.getId());
    tmp.setMaterialsName(mmf.getMaterialsName());
    tmp.setStatus(Integer.parseInt(mmf.getStatus()));
    tmp.setCreatedAt(mmf.getCreatedAt());
    tmp.setUpdatedAt(mmf.getUpdatedAt());
    tmp.setUpdatedMstUserId(mmf.getUpdatedMstUserId());
    return tmp;
  }

  public void download(List<MstMaterialsManagement> materials) {
    try {
      fileService.exportDataTypeExcel(materials, "materials");
    } catch (IOException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }

  public void upload() {
    fileService.setSheetName("materials");
    int a = fileService.getRowNum();
    for (int i = 1; i <= a; i++) {
      mstMaterialsRepository
          .save(fileService.importDataToRowTypeExcel(new MstMaterialsManagement(), "materials", i));
    }
  }

  public List<MstMaterialsManagement> changeMaterialsForm(
      List<MstMaterialsManagementForm> materialsForm) {
    List<MstMaterialsManagement> materials = new ArrayList<>();
    for (MstMaterialsManagementForm mf : materialsForm) {
      materials.add(updateMaterials(mf));
    }
    return materials;
  }

  public MstMaterialsManagementForm showMaterialsForm() {
    MstMaterialsManagementForm tmp = new MstMaterialsManagementForm();
    return tmp;
  }
}
