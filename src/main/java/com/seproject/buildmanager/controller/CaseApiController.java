package com.seproject.buildmanager.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.service.MstMatterService;

@RestController
@RequestMapping("/api/case")

public class CaseApiController {

  @Autowired
  private MstMatterService mstCaseService;

  @PutMapping("Status")
  public ResponseEntity<String> toggleCaseStatus(@RequestBody Map<String, Integer> request) {
    Integer id = request.get("id");
    try {
      mstCaseService.saveStatus(id);
      return ResponseEntity.ok("Status toggled for case with ID" + id);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ステータスの変更に失敗しました");
    }
  }

  @PostMapping("search")
  public ResponseEntity<String> search(@RequestBody Map<String, String> request) {
    String[] search = request.get("search").split(",");
    String view = search[0];
    String id = search[1];
    try {
      mstCaseService.save(Integer.parseInt(id), view);
      return ResponseEntity.ok("Status toggled for case with ID");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ステータスの変更に失敗しました");
    }
  }

}
