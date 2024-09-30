package com.seproject.buildmanager.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seproject.buildmanager.service.MstAcceptingOrderService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/accepting-order")
public class AcceptingOrderApiController {


  private final MstAcceptingOrderService mstAcceptingOrderService;

  public AcceptingOrderApiController(MstAcceptingOrderService mstAcceptingOrderService) {
    this.mstAcceptingOrderService = mstAcceptingOrderService;
  }

  @PostMapping("/orderStatus")
  public ResponseEntity<?> cancelOrder(@RequestBody Map<String, Object> request) {
    int orderId = Integer.parseInt(request.get("orderId").toString());
    int status = Integer.parseInt(request.get("status").toString());

    try {
      mstAcceptingOrderService.orderStatus(orderId, status);
      return ResponseEntity.ok("Order status updated successfully");
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
    }
  }

  @PostMapping("/history/add")
  public ResponseEntity<?> addOrderHistory(@RequestBody Map<String, Object> request) {
    int orderId = Integer.parseInt(request.get("orderId").toString());
    String updateContent = request.get("updateContent").toString();

    try {
      mstAcceptingOrderService.addOrderHistory(orderId, updateContent);
      return ResponseEntity.status(HttpStatus.CREATED).body("History added successfully");
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to add history: " + e.getMessage());
    }
  }
}
