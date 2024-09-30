package com.seproject.buildmanager.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.service.MstConstructionService;

@RestController
@RequestMapping("api-construction")
public class ConstructionApiController {

  private final MstConstructionService mstConstructionService;

  private ConstructionApiController(MstConstructionService mstConstructionService) {
    this.mstConstructionService = mstConstructionService;
  }

  @PostMapping("")
  public ResponseEntity<String> toggleConstructionStatus(@RequestBody Map<String, String> request) {
    String name = request.get("name");

    if (name.equals("")) {
      name = null;
    }
    try {

      mstConstructionService.saveConstructionRegister(name);
      return ResponseEntity.ok("Status toggled for user with ID " + name);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("工事区分分類を入力してください");
    }
  }

  @PostMapping("construction-update")
  public ResponseEntity<String> toggleConstructionUpdateStatus(
      @RequestBody Map<String, String> request) {
    String[] update1 = request.get("update").split(",");
    String update = update1[0];
    String id = update1[1];
    try {

      mstConstructionService.saveConstructionUpdate(update, Integer.parseInt(id));
      return ResponseEntity.ok("Status toggled for user with ID " + update);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling status");
    }
  }

  // @PostMapping("KojiKubunBunrui") // フォームに入力されたグループ名を受け取ってserviceメソッドを実行して、同じ画面へ遷移する
  // public String saveCost_group_name(@RequestParam("koji_kubun_bunrui_save") String
  // cost_group_name,
  // Model model) {
  // mstConstructionService.saveConstructionRegister(cost_group_name);
  // return "redirect:/koji_kubun_bunrui";
  // }

  @PostMapping("status")
  public ResponseEntity<String> toggleUserStatus(@RequestBody Map<String, Integer> request) {
    Integer ConstructionId = request.get("id");
    try {
      mstConstructionService.toggleStatus(ConstructionId);
      return ResponseEntity.ok("Status toggled for owner with ID " + ConstructionId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling status");
    }
  }

  // @PostMapping("upload")
  // public ResponseEntity<String> uploadFile() {
  // try {
  // ExcelFileService<MstConstruction> fileService =
  // new ExcelFileService<MstConstruction>("工事区分分類管理データ.xlsx");
  // fileService.setSheetName("sheet1");
  // int rowNum = fileService.getRowNum();
  // for (int i = 1; i < rowNum + 1; i++) {
  // mstConstructionRepository
  // .save(fileService.importDataToRowTypeExcel(new MstConstruction(), "sheet1", i));
  // }
  // fileService.release();
  // return ResponseEntity.ok("Status toggled for owner with ID " + "1");
  // } catch (Exception e) {
  // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ステータスの変更に失敗しました");
  // }
  // }



}
