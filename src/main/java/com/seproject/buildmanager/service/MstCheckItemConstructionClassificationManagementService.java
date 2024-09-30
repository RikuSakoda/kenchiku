package com.seproject.buildmanager.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.entity.MstCheck;
import com.seproject.buildmanager.entity.MstCheckItemConstructionClassificationManagement;
import com.seproject.buildmanager.entity.MstConstruction;
import com.seproject.buildmanager.entity.MstConstructionClassificationManagement;
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.form.MstCheckItemConstructionClassificationManagementForm;
import com.seproject.buildmanager.repository.MstCheckItemConstructionClassificationManagementRepository;

@Service
public class MstCheckItemConstructionClassificationManagementService {

  private static final Logger logger =
      LoggerFactory.getLogger(MstCheckItemConstructionClassificationManagementService.class);

  private final MstCheckItemConstructionClassificationManagementRepository mstCheckItemConstructionClassificationManagementRepository;

  private final MstConstructionClassificationManagementService mstConstructionClassificationManagementService;

  private final MstCustomerService mstCustomerService;

  private final MstCheckService mstCheckService;

  private final MstConstructionService mstConstructionService;


  private MstCheckItemConstructionClassificationManagementService(
      MstCheckItemConstructionClassificationManagementRepository mstCheckItemConstructionClassificationManagementRepository,
      MstConstructionClassificationManagementService mstConstructionClassificationManagementService,
      MstCustomerService mstCustomerService, MstCheckService mstCheckService,
      MstConstructionService mstConstructionService) {
    this.mstCheckItemConstructionClassificationManagementRepository =
        mstCheckItemConstructionClassificationManagementRepository;
    this.mstConstructionClassificationManagementService =
        mstConstructionClassificationManagementService;
    this.mstCustomerService = mstCustomerService;
    this.mstCheckService = mstCheckService;
    this.mstConstructionService = mstConstructionService;
  }



  // 顧客
  public List<MstCustomer> getCustmerAll() {

    List<MstCustomer> customer = mstCustomerService.getAllCustomers();

    return customer;

  }

  public List<MstCheck> getCheckAll() {

    List<MstCheck> check = mstCheckService.getAllChecks();

    return check;

  }

  public List<MstConstructionClassificationManagement> getCostAll() {

    List<MstConstructionClassificationManagement> cost =
        mstConstructionClassificationManagementService.getAllCost();

    return cost;
  }

  public List<MstConstruction> getConstuctionAll() {

    List<MstConstruction> constuction = mstConstructionService.getAllConstructions();

    return constuction;


  }



  public List<MstCheckItemConstructionClassificationManagement> getCheckCostAll() {

    List<MstCheckItemConstructionClassificationManagement> checkcost =
        mstCheckItemConstructionClassificationManagementRepository.findAll();
    return checkcost;
  }

  public MstCheckItemConstructionClassificationManagementForm CheckCostForm() { // インスタンス生成
    MstCheckItemConstructionClassificationManagementForm tmp =
        new MstCheckItemConstructionClassificationManagementForm();
    return tmp;
  }

  public MstCheckItemConstructionClassificationManagement getCheckCost(int id) {// id検索
    return mstCheckItemConstructionClassificationManagementRepository.findById(id)
        .orElse(new MstCheckItemConstructionClassificationManagement());
  }



  public List<MstCheckItemConstructionClassificationManagement> checkcustomer(int custmoerid,
      int checkid) {
    List<MstCheckItemConstructionClassificationManagement> checkcustomer =
        mstCheckItemConstructionClassificationManagementRepository.checkcost(custmoerid, checkid);
    if (checkcustomer == null || checkcustomer.size() == 0) {
      checkcustomer = new ArrayList<MstCheckItemConstructionClassificationManagement>();
      MstCheckItemConstructionClassificationManagement mstCheckCost =
          new MstCheckItemConstructionClassificationManagement();
      mstCheckCost.setCheckId(checkid);
      mstCheckCost.setCustomerId(custmoerid);
      mstCheckCost.setStatus(0);
      mstCheckCost.setConstructionClassificationId(1);
      mstCheckCost.setConstractionId(1);
      checkcustomer.add(mstCheckCost);
    }


    return checkcustomer;

  }

  public MstCheckItemConstructionClassificationManagement saveDefault(int custmoerid, int checkid) {
    MstCheckItemConstructionClassificationManagement mstCheckCost =
        new MstCheckItemConstructionClassificationManagement();
    mstCheckCost.setCheckId(checkid);
    mstCheckCost.setCustomerId(custmoerid);
    mstCheckCost.setStatus(0);
    mstCheckCost.setConstructionClassificationId(1);
    mstCheckCost.setConstractionId(1);
    return mstCheckItemConstructionClassificationManagementRepository.save(mstCheckCost);
  }

  public List<MstCheckItemConstructionClassificationManagement> saveAll(
      List<MstCheckItemConstructionClassificationManagement> mstcheckcost) {


    return mstCheckItemConstructionClassificationManagementRepository.saveAll(mstcheckcost);
  }

  public void saveCheck(String[] id, String[] status, String[] cost, String[] constraction,
      String checkId, int cusId) {
    List<MstCheckItemConstructionClassificationManagement> checkcost = new ArrayList<>();
    MstCheckItemConstructionClassificationManagement checkCost =
        new MstCheckItemConstructionClassificationManagement();
    for (String c : status) {
      if (c.equals("(")) {
        checkCost.setStatus(1);
      } else if (c.equals(")")) {
        checkcost.add(checkCost);
        checkCost = new MstCheckItemConstructionClassificationManagement();
      } else {
        checkCost.setStatus(0);
      }
    }

    String firstId = id[0];
    for (int i = 0; i < id.length; i++) {
      if (firstId.equals(id[i])) {
        id[i] = null;
      }
    }
    id[0] = firstId;

    for (int i = 0; i < checkcost.size(); i++) {
      if (id[i] != null) {
        checkcost.get(i).setId(Integer.parseInt(id[i]));
      }
      checkcost.get(i).setCheckId(Integer.parseInt(checkId));
      checkcost.get(i).setCustomerId(cusId);
      checkcost.get(i).setConstructionClassificationId(Integer.parseInt(cost[i]));
      checkcost.get(i).setConstractionId(Integer.parseInt(constraction[i]));
    }

    this.saveAll(checkcost);

  }

  public List<MstCheckItemConstructionClassificationManagement> searchCustomer(Integer i) {
    return mstCheckItemConstructionClassificationManagementRepository.searchCosutomerId(i);
  }

}


