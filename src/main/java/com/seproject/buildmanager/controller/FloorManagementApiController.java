package com.seproject.buildmanager.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.service.MstFloorManagementService;

@RestController
@RequestMapping("/floor-management/api")
public class FloorManagementApiController {

  @Autowired
  private MstFloorManagementService mstFloorManagementService;

  @PutMapping("toggle-status")
  public ResponseEntity<String> toggleFloorStatus(@RequestBody Map<String, Integer> request) {
    Integer floorId = request.get("floorId");

    try {
      mstFloorManagementService.saveStatus(floorId);
      return ResponseEntity.ok("Status toggled for floorManagement with ID " + floorId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling status");
    }
  }
}
