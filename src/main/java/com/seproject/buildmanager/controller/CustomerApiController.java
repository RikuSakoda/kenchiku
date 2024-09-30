package com.seproject.buildmanager.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.service.MstCustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerApiController {


  private final MstCustomerService mstCustomerService;

  public CustomerApiController(MstCustomerService mstCustomerService) {
    this.mstCustomerService = mstCustomerService;
  }

  @PutMapping("toggle-status")
  public ResponseEntity<String> toggleCustomerStatus(@RequestBody Map<String, Integer> request) {
    Integer custId = request.get("custId");
    try {
      mstCustomerService.saveStatus(custId);
      return ResponseEntity.ok("Status toggled for customer with ID " + custId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling status");
    }
  }
}
