package com.seproject.buildmanager.controller;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.common.ExcelFileService;
import com.seproject.buildmanager.entity.MstCheck;
import com.seproject.buildmanager.form.MstCheckForm;
import com.seproject.buildmanager.repository.MstCheckRepository;
import com.seproject.buildmanager.service.MstCheckService;

@RestController
@RequestMapping("/api/check")
public class CheckApiController {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  @Autowired
  private MstCheckRepository mstcheckRepository;

  @Autowired
  private MstCheckService mstCheckService;


  public CheckApiController(MstCheckRepository mstcheckRepository,
      MstCheckService mstCheckService) {
    super();
    this.mstcheckRepository = mstcheckRepository;
    this.mstCheckService = mstCheckService;
  }

  @PostMapping("check-register")
  public ResponseEntity<String> registerCheck(@RequestBody Map<String, String> request) {

    logger.info("--- CheckApiController.registerCheck START ---");

    String name = request.get("name");
    name = name.equals("") ? null : name;

    try {
      if (name == null) {
        throw new IllegalArgumentException();
      }

      this.mstCheckService.saveCheck(name);

      logger.info("--- CheckApiController.registerCheck END ---");
      return ResponseEntity.ok("Register check : " + name);
    } catch (Exception e) {

      logger.info("--- CheckApiController.registerCheck END ... ERROR ---");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("リフォーム・修繕箇所チェック項目を入力してください");
    }

  }

  @PutMapping("status")
  public ResponseEntity<String> toggleStatus(@RequestBody Map<String, String> request) {
    int id = Integer.parseInt(request.get("id"));
    try {
      this.mstcheckRepository.toggleStatus(id);
      return ResponseEntity.ok("Check updated with ID " + id);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating Check");
    }
  }

  @PostMapping("upload")
  public ResponseEntity<String> uploadFile() {
    try {
      ExcelFileService<MstCheck> fileService = new ExcelFileService<MstCheck>("チェック項目管理.xlsx");

      fileService.setSheetName("sheet1");
      int rowNum = fileService.getRowNum();
      for (int i = 1; i < rowNum + 1; i++) {
        // ownerList.add(fileService.importDataToRowTypeExcel(new MstOwner(), "sheet1", i));
        mstcheckRepository.save(fileService.importDataToRowTypeExcel(new MstCheck(), "sheet1", i));
      }
      return ResponseEntity.ok("Status toggled for owner with ID " + "1");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ステータスの変更に失敗しました");
    }
  }

  @PostMapping("check-update")
  public ResponseEntity<String> updateCheck(@RequestBody MstCheckForm request) {
    int id = request.getId();
    String checkDetail = request.getCheckDetail();

    checkDetail = checkDetail.equals("") ? null : checkDetail;
    try {
      if (checkDetail == null) {
        throw new IllegalArgumentException();
      }
      this.mstCheckService.saveCheckUpdate(checkDetail, id);
      return ResponseEntity.ok("Status toggled for user with ID " + id);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("変更項目を入力してください");
    }
  }
}
