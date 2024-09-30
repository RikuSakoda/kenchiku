package com.seproject.buildmanager.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.entity.MstCheck;
import com.seproject.buildmanager.form.MstCheckForm;
import com.seproject.buildmanager.repository.MstCheckRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class MstCheckService
    implements RegistUploadFile, MstSearchService<MstCheckForm, MstCheckForm> {

  private static final Logger logger = LoggerFactory.getLogger(MstCheckService.class);

  private final MstCheckRepository mstCheckRepository;

  private final CommonService commonService;

  public MstCheckService(MstCheckRepository mstCheckRepository, CommonService commonService) {
    this.mstCheckRepository = mstCheckRepository;
    this.commonService = commonService;
  }

  public MstCheckForm mstCheckForm() {
    MstCheckForm mstCheckForm = new MstCheckForm();
    return mstCheckForm;
  }

  @Transactional(readOnly = true)
  public List<MstCheck> getAllChecks() {
    logger.info("--- MstCheckService.getAllChecks START ---");
    try {
      List<MstCheck> checks = this.mstCheckRepository.findAll();
      logger.info("--- MstCheckService.getAllChecks END ---");
      return checks;
    } catch (Exception e) {
      logger.error("Error in getAllChecks: {}", e.getMessage());
      throw new RuntimeException("Failed to retrieve checks", e);
    }
  }

  public void saveCheck(String savecheck) {
    MstCheck check = new MstCheck();
    check.setCheckDetail(savecheck);
    check.setStatus(Constants.STATUS_TRUE);
    check.setUpdatedMstUserId(this.commonService.getLoginUserId());
    check.setCreatedAt(LocalDateTime.now());
    check.setUpdatedAt(LocalDateTime.now());
    this.mstCheckRepository.save(check);
  }

  @Transactional
  public MstCheck saveCheckUpdate(String name, Integer id) { // 変更をする処理
    logger.info("--- MstCheckService.saveCheckUpdate START ---");
    MstCheck check = this.mstCheckRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Status toggled for user with ID " + id));
    check.setCheckDetail(name);
    check.setUpdatedMstUserId(this.commonService.getLoginUserId());
    check.setUpdatedAt(LocalDateTime.now());
    MstCheck updatedCheck = this.mstCheckRepository.save(check);
    logger.info("--- MstCheckService.saveCheckUpdate END ---");
    return updatedCheck;
  }


  public void poi(HttpServletResponse response) throws IOException {
    // エクセルファイル生成
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Checks Data");
    ServletOutputStream outputStream = response.getOutputStream();
    try {
      // ヘッダー行を作成
      createHeaderRow(sheet);

      // データベースからチェック項目のリストを取得
      List<MstCheck> checks = getAllChecks();

      // エクセルファイルにデータを書き込む
      int rowNum = 1;
      for (MstCheck check : checks) {
        Row row = sheet.createRow(rowNum++);
        writeCheckDataToRow(check, row);
      }

      // ファイル名に2バイト文字を使えるように一工夫
      response.addHeader("Content-Disposition", "attachment; filename*=UTF-8''"
          + URLEncoder.encode("チェック項目管理データ.xlsx", StandardCharsets.UTF_8.name()));

      // エクセルファイルを書き込み、リソースを閉じる
      workbook.write(outputStream);
      outputStream.close();
      workbook.close();
    } catch (IOException e) {
      // エクセルファイルの生成に失敗した場合のエラーハンドリング
      logger.error("Error generating Excel file", e);
      throw new RuntimeException("エクセルファイルの生成に失敗しました。", e);
    } finally {
      // リソースのクリーンアップ
      if (outputStream != null) {
        outputStream.close();
      }
      if (workbook != null) {
        workbook.close();
      }
    }
  }

  private void writeCheckDataToRow(MstCheck check, Row row) {
    // 各列にチェック項目のデータを書き込む
    row.createCell(0).setCellValue(check.getId());
    row.createCell(1).setCellValue(check.getCheckDetail());
    row.createCell(2).setCellValue(check.getStatus());
    row.createCell(3).setCellValue(check.getCreatedAt().toString());
    if (check.getUpdatedAt() != null) {
      row.createCell(4).setCellValue(check.getUpdatedAt().toString());
    } else {
      row.createCell(4).setCellValue("NULL");
    }
    row.createCell(5).setCellValue(check.getUpdatedMstUserId());
  }

  private void createHeaderRow(Sheet sheet) {
    // ヘッダー行を作成し、各列の名前を設定
    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("ID");
    headerRow.createCell(1).setCellValue("CheckDetail");
    headerRow.createCell(2).setCellValue("Status");
    headerRow.createCell(3).setCellValue("Created At");
    headerRow.createCell(4).setCellValue("Updated At");
    headerRow.createCell(5).setCellValue("Updated Mst User ID");
  }

  @Override
  public void registUploadFile(List<Object> object) {

    if (object != null && object.size() != 0) {
      MstCheck mstCheck = new MstCheck();
      mstCheck.setCheckDetail(String.valueOf(object.get(1)));
      mstCheck.setStatus((int) Double.parseDouble((String.valueOf(object.get(2)))));
      String localtime = String.valueOf(object.get(3));
      localtime = localtime.replace("T", " ");
      LocalDateTime localDateTime = null;
      try {
        localDateTime =
            LocalDateTime.parse(localtime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
      } catch (DateTimeParseException e) {
        localDateTime =
            LocalDateTime.parse(localtime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      }
      mstCheck.setCreatedAt(localDateTime);
      mstCheck.setUpdatedMstUserId(1);
      mstCheckRepository.save(mstCheck);
    }

  }

  /**
   * チェックグループを検索します。
   * 
   * @param mstCheckGroupForm 検索条件を含むフォーム
   * @return 検索結果のチェックグループフォームのリスト
   */
  @Override
  public List<MstCheckForm> search(MstCheckForm mstCheckForm) {
    String checkDetail = "";
    String status = "";
    String createdAt = "";
    String updatedAt = "";

    MstCheckForm check = mstCheckForm;

    checkDetail += nullCheck(check.getCheckDetail());
    if (check.getStatus() == null || check.getStatus().equals("")) {
      status += "";
    } else {
      if (check.getStatus().equals("active")) {
        status += "1";
      } else {
        status += "0";
      }
    }
    createdAt += nullCheck(check.getCreatedAt1());
    updatedAt += nullCheck(check.getUpdatedAt1());

    List<MstCheck> a = mstCheckRepository.search(checkDetail, status, createdAt, updatedAt);

    List<MstCheckForm> mstCheckFormList = new ArrayList<>();
    for (MstCheck check1 : a) {
      mstCheckFormList.add(updateCheckForm(check1.getId()));
    }
    return mstCheckFormList;
  }

  public MstCheckForm updateCheckForm(Integer id) {
    MstCheck mstCheck = mstCheckRepository.findById(id).orElse(new MstCheck());
    MstCheckForm tmp = new MstCheckForm();
    tmp.setId(mstCheck.getId());
    tmp.setCheckDetail(mstCheck.getCheckDetail());
    tmp.setStatus(mstCheck.getStatus().toString());
    tmp.setCreatedAt(mstCheck.getCreatedAt());
    tmp.setUpdatedAt(mstCheck.getUpdatedAt());
    return tmp;
  }

  public List<MstCheck> CheckItemByGroupIdAndCustomerId(int groupId, int customerId) {
    return this.mstCheckRepository.getCheckItemByGroupIdAndCustomerId(groupId, customerId);
  }
}


