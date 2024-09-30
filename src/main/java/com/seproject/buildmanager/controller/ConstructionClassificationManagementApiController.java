package com.seproject.buildmanager.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.repository.MstConstructionClassificationManagementRepository;
// import com.seproject.buildmanager.repository.MstOwnerRepository;
import com.seproject.buildmanager.service.MstConstructionClassificationManagementService;

@RestController
@RequestMapping("/api/cost")
public class ConstructionClassificationManagementApiController {

  private final MstConstructionClassificationManagementService mstCostService;

  @Autowired
  private MstConstructionClassificationManagementRepository mstCostRepository;

  public ConstructionClassificationManagementApiController(
      MstConstructionClassificationManagementService mstCostService) {
    this.mstCostService = mstCostService;
  }

  // @Autowired
  // private MstOwnerRepository mstOwnerRepository;

  @PutMapping("n")
  public ResponseEntity<String> toggleCostStatus(@RequestBody Map<String, Integer> request) {
    Integer costId = request.get("costId");
    try {
      mstCostRepository.toggleStatus(costId);
      // mstOwnerRepository.toggleStatus2(costId);
      return ResponseEntity.ok("Status toggled for cost with ID " + costId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling status");
    }
  }

  @PutMapping("b")
  public ResponseEntity<String> Cost(@RequestBody Map<String, String> request) {
    String[] costId = request.get("a").split(",");
    String id = costId[0];
    String view = costId[1];
    try {
      mstCostService.CostUpdate(view, Integer.parseInt(id));
      return ResponseEntity.ok("Status toggled for cost with ID " + view);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error toggling status");
    }
  }
}
