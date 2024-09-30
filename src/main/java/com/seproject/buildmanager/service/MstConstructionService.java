package com.seproject.buildmanager.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.common.ExcelFileService;
import com.seproject.buildmanager.entity.MstConstruction;
import com.seproject.buildmanager.form.MstConstructionForm;
import com.seproject.buildmanager.repository.MstConstructionRepository;

@Service
public class MstConstructionService
    implements MstSearchService<MstConstructionForm, MstConstruction> {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstConstructionRepository mstConstructionRepository;

  private final CommonService commonService;

  private final ExcelFileService<MstConstruction> excelFileService;

  private MstConstructionService(MstConstructionRepository mstConstructionRepository,
      ExcelFileService<MstConstruction> excelFileService, CommonService commonService) {
    this.mstConstructionRepository = mstConstructionRepository;
    this.commonService = commonService;
    this.excelFileService = excelFileService;
    this.excelFileService.initializeExcel("工事区分分類管理データ.xlsx");
  }

  public MstConstructionForm mstConstructionForm() {
    MstConstructionForm mstConstructionForm = new MstConstructionForm();
    return mstConstructionForm;
  }

  public MstConstruction getConstructionById(Integer id) {
    return mstConstructionRepository.findById(id).orElse(new MstConstruction());
  }

  public MstConstruction saveConstructionRegister(String name) {
    logger.info("--- MstConstructionService.saveConstructionRegister START ---");
    MstConstruction tmp = new MstConstruction();
    tmp.setCostGroupName(name);
    try {
      tmp.setStatus(Constants.STATUS_TRUE);
    } catch (NumberFormatException e) {
      logger.error("Error status MstConstruction : " + tmp.toString(), e);
      throw new RuntimeException("ステータスが正常ではありません", e);
    }
    tmp.setCreatedAt(LocalDateTime.now());
    tmp.setUpdatedAt(LocalDateTime.now());
    tmp.setUpdatedMstUserId(this.commonService.getLoginUserId());

    MstConstruction result = mstConstructionRepository.save(tmp);
    logger.info("--- MstConstructionService.saveConstructionRegister END ---");
    return result;
  }

  public MstConstruction getConstructionByName(String name) {
    List<MstConstruction> cost = mstConstructionRepository.getConstructionByName(name);
    if (cost.size() != 0) {
      return cost.get(0);
    } else {
      return null;
    }
  }

  public List<MstConstruction> getAllConstructions() {
    logger.info("--- MstCheckGroupeService.getAllGroupes START ---");
    List<MstConstruction> constructions = mstConstructionRepository.findAll();
    logger.info("--- MstUserService.getAllGroupes END ---");
    return constructions;
  }

  public MstConstruction saveConstructionUpdate(String name, Integer id) {
    logger.info("--- MstConstructionService.saveConstructionUpdate START ---");
    MstConstruction a = mstConstructionRepository.findById(id).orElse(new MstConstruction());
    MstConstruction tmp = new MstConstruction();
    tmp.setId(id);
    tmp.setCostGroupName(name);
    try {
      tmp.setStatus(a.getStatus());
    } catch (NumberFormatException e) {
      logger.error("Error Status MstConstruction : " + tmp.toString(), e);
      throw new RuntimeException("ステータスの変更に失敗しました。", e);
    }
    tmp.setCreatedAt(a.getCreatedAt());
    tmp.setUpdatedAt(LocalDateTime.now());
    tmp.setUpdatedMstUserId(1);
    // tmp.setUpdatedMstUserId(this.commonService.getLoginUserId());// userのidがわからないため保留
    MstConstruction update = mstConstructionRepository.save(tmp);
    logger.info("--- MstConstructionService.saveConstructionUpdate END ---");
    return update;
  }

  @Override
  public List<MstConstruction> search(MstConstructionForm t) {
    String costGroupName = "";
    String status = "";
    String createdAt = "";
    String updatedAt = "";

    costGroupName = nullCheck(t.getCostGroupName());
    if (t.getStatus() == null || t.getStatus().equals("")) {
      status += "";
    } else {
      if (t.getStatus().equals("active")) {
        status += "1";
      } else {
        status += "0";
      }
    }
    createdAt = nullCheck(t.getCreatedAt1());
    updatedAt = nullCheck(t.getUpdatedAt1());
    List<MstConstruction> a =
        mstConstructionRepository.search(costGroupName, status, createdAt, updatedAt);
    return a;
  }

  public void download(List<MstConstruction> constructions) {
    try {
      this.excelFileService.exportDataTypeExcel(constructions, "constructions");
    } catch (IOException e) {

      e.printStackTrace();
    }
  }

  public void upload() {
    this.excelFileService.setSheetName("constructions");
    int a = this.excelFileService.getRowNum();
    for (int i = 1; i <= a; i++) {
      mstConstructionRepository.save(this.excelFileService
          .importDataToRowTypeExcel(new MstConstruction(), "constructions", i));
    }
  }

  public void toggleStatus(Integer ConstructionId) {
    mstConstructionRepository.toggleStatus(ConstructionId);
  }


}
