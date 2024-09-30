package com.seproject.buildmanager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.entity.MstConstruction;
import com.seproject.buildmanager.entity.MstConstructionClassificationManagement;
import com.seproject.buildmanager.entity.MstEstimateItem;
import com.seproject.buildmanager.entity.MstEstimateManagement;
import com.seproject.buildmanager.service.MstConstructionClassificationManagementService;
import com.seproject.buildmanager.service.MstConstructionService;
import com.seproject.buildmanager.service.MstEstimateItemService;
import com.seproject.buildmanager.service.MstEstimateManagementService;

@RestController
@RequestMapping("/api/estimate")
public class EstimateManagementApiController {

  private final MstEstimateManagementService estimateManagementService;

  private final MstEstimateItemService mstEstimateItemService;

  private final MstConstructionService mstConstructionService;

  private final MstConstructionClassificationManagementService costClassificationService;



  public EstimateManagementApiController(MstEstimateManagementService estimateManagementService,
      MstEstimateItemService mstEstimateItemService, MstConstructionService mstConstructionService,
      MstConstructionClassificationManagementService costClassificationService) {
    this.estimateManagementService = estimateManagementService;
    this.mstEstimateItemService = mstEstimateItemService;
    this.mstConstructionService = mstConstructionService;
    this.costClassificationService = costClassificationService;

  }

  // 見積もりアイテム一覧を取得するAPI
  @GetMapping("/items")
  public ResponseEntity<List<MstEstimateItem>> getEstimateItems(@RequestParam("matterId") String id,
      @RequestParam("version") String var) {
    List<MstEstimateItem> estimateItems =
        mstEstimateItemService.findAllByVerId(Integer.parseInt(id), Integer.parseInt(var));
    return new ResponseEntity<>(estimateItems, HttpStatus.OK);
  }

  @GetMapping("/get-construction-name")
  public ResponseEntity<List<String>> getConstructionName() {
    List<MstConstruction> construction = mstConstructionService.getAllConstructions();
    List<String> names = new ArrayList<String>();
    for (MstConstruction cost : construction) {
      names.add(cost.getCostGroupName());
    }
    return new ResponseEntity<>(names, HttpStatus.OK);
  }

  @GetMapping("/get-construction-map")
  public ResponseEntity<Map<Integer, String>> getConstructionMap() {
    List<MstConstruction> construction = mstConstructionService.getAllConstructions();
    Map<Integer, String> names = new HashMap<Integer, String>();
    for (MstConstruction cost : construction) {
      names.put(cost.getId(), cost.getCostGroupName());
    }
    return new ResponseEntity<>(names, HttpStatus.OK);
  }

  @GetMapping("/get-costclassification-name")
  public ResponseEntity<List<String>> getConstructionClassificationName() {
    List<MstConstructionClassificationManagement> costClassification =
        costClassificationService.getAllCost();
    List<String> names = new ArrayList<String>();
    for (MstConstructionClassificationManagement cost : costClassification) {
      names.add(cost.getCostContents());
    }
    return new ResponseEntity<>(names, HttpStatus.OK);
  }

  @GetMapping("/get-costclassification-name-id")
  public ResponseEntity<List<String>> getConstructionClassificationId(
      @RequestParam("construcationName") String name) {
    List<MstConstructionClassificationManagement> costClassification =
        costClassificationService.getAllCost();
    List<String> names = new ArrayList<String>();
    for (MstConstructionClassificationManagement cost : costClassification) {
      if (cost.getGroupName().equals(name)) {
        names.add(cost.getCostContents());
      }
    }
    return new ResponseEntity<>(names, HttpStatus.OK);
  }


  @GetMapping("/new-row")
  public ResponseEntity<MstEstimateItem> getRegistEstimateItems() {
    MstEstimateItem estimateItems = mstEstimateItemService.registForm();
    return new ResponseEntity<>(estimateItems, HttpStatus.OK);
  }

  @PostMapping("/items2")
  public ResponseEntity<String> postEstimateItems(@RequestBody Map<String, String[][]> request) {
    String[][] values = request.get("name");
    List<MstEstimateItem> items = new ArrayList<MstEstimateItem>();
    try {
      for (int i = 0; i < values.length - 1; i++) {
        MstEstimateItem item = mstEstimateItemService.convertStringToEntity(values[i],
            Integer.parseInt(values[values.length - 1][0]));
        items.add(item);
      }
    } catch (NullPointerException | NumberFormatException e) {
      return new ResponseEntity<>("エラー", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    MstEstimateManagement estimate = estimateManagementService
        .saveNewEstimate(Integer.parseInt(values[values.length - 1][0]), items);
    for (MstEstimateItem item : items) {
      item.setEstimateId(estimate);
      mstEstimateItemService.save(item);
    }
    return new ResponseEntity<>("おーるぐりーん", HttpStatus.OK);
  }
}
