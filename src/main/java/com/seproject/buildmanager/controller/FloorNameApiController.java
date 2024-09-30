package com.seproject.buildmanager.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.service.FloorNameService;
import jakarta.transaction.Transactional;

@RestController
public class FloorNameApiController {

  private final FloorNameService floorNameService;

  public FloorNameApiController(FloorNameService floorNameService) {
    super();
    this.floorNameService = floorNameService;
  }

  @Transactional
  @PostMapping("floor-name-save")
  public ResponseEntity<String> toggleFloorNameSave(@RequestBody Map<String, String> request)
      throws NullPointerException {
    String name = request.get("name");
    name = name.equals("") ? null : name;

    try {
      if (name == null) {
        throw new NullPointerException();
      }
      floorNameService.saveFloorName(name);
      return ResponseEntity.ok("Status toggled for user with ID " + name);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("物件間取り名称を入力してください");
    }

  }

  @Transactional
  @PostMapping("floor-name-update")
  public ResponseEntity<String> toggleFloorNameUpdate(@RequestBody Map<String, String> request)
      throws NullPointerException {
    String[] update1 = request.get("update").split(",");
    String update = update1[0];
    String id = update1[1];
    update = update.equals("") ? null : update;
    try {
      if (update == null) {
        throw new NullPointerException();
      }
      floorNameService.updateFloorName(update, Integer.parseInt(id));
      return ResponseEntity.ok("Status toggled for user with ID " + update);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("変更する物件間取り名称を入力してください");
    }
  }


  @Transactional
  @PostMapping("floor-name-status")
  public ResponseEntity<String> toggleFloorNameStatus(@RequestBody Map<String, String> request) {
    int id = Integer.parseInt(request.get("id"));
    try {
      floorNameService.updateFloorName(id);
      floorNameService.updateStatusFloorName(id);
      floorNameService.toggleStatus(id);
      return ResponseEntity.ok("Floor Name updated with ID " + id);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error updating Floor Name");
    }
  }

}
