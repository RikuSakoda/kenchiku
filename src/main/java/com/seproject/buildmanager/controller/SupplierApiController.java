package com.seproject.buildmanager.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.service.MstSupplierManagementService;

@RestController
@RequestMapping("/api/supplier")
public class SupplierApiController {

  private final MstSupplierManagementService mstSupplierManagementService;

  public SupplierApiController(MstSupplierManagementService mstSupplierManagementService) {
    super();
    this.mstSupplierManagementService = mstSupplierManagementService;
  }

  @PutMapping("status")
  public ResponseEntity<String> toggleCostStatus(@RequestBody Map<String, Integer> request) {
    Integer supplierId = request.get("supplierId");
    try {
      mstSupplierManagementService.toggleStatus(supplierId);
      return ResponseEntity.ok("Status toggled for supplier with ID " + supplierId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling status");
    }
  }

}

