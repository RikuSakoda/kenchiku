package com.seproject.buildmanager.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.MstCodeEnums;
import com.seproject.buildmanager.entity.MstAcceptingOrder;
import com.seproject.buildmanager.entity.MstAcceptingOrderDetail;
import com.seproject.buildmanager.entity.MstAcceptingOrderHistory;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.entity.MstMatter;
import com.seproject.buildmanager.form.MstAcceptingOrderDetailForm;
import com.seproject.buildmanager.form.MstAcceptingOrderForm;
import com.seproject.buildmanager.form.MstMatterForm;
import com.seproject.buildmanager.repository.MstAcceptingOrderDetailRepository;
import com.seproject.buildmanager.repository.MstAcceptingOrderHistoryRepository;
import com.seproject.buildmanager.repository.MstAcceptingOrderRepository;
import com.seproject.buildmanager.repository.MstCodeRepository;
import com.seproject.buildmanager.repository.MstMatterRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class MstAcceptingOrderService implements MstSearchService<MstMatterForm, MstMatterForm> {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstAcceptingOrderRepository mstAcceptingOrderRepository;

  private final MstAcceptingOrderDetailRepository mstAcceptingOrderDetailRepository;

  private final MstAcceptingOrderHistoryRepository mstAcceptingOrderHistoryRepository;

  private final MstMatterRepository mstCaseRepository;

  private final MstCodeService mstCodeService;

  private final MstMatterRepository mstMatterRepository;

  private final MstSupplierManagementService mstSupplierManagementService;

  private final MstCodeRepository mstCodeRepesitory;

  private final CommonService commonService;

  // Enumから種別を取得するためののcode_kindの値を取得
  private static final int TASK_SUBSTANCE = MstCodeEnums.TASK_SUBSTANCE.getValue();

  // 状況ステータス取得
  private static final int SITUATION_STATUS = MstCodeEnums.SITUATION_STATUS.getValue();

  private static final int ORDER_STATUS = MstCodeEnums.ORDER_STATUS.getValue();

  private static final int UNIT = MstCodeEnums.UNIT.getValue();



  private MstAcceptingOrderService(MstAcceptingOrderRepository mstAcceptingOrderRepository,
      MstAcceptingOrderDetailRepository mstAcceptingOrderDetailRepository,
      MstAcceptingOrderHistoryRepository mstAcceptingOrderHistoryRepository,
      MstMatterRepository mstCaseRepository, MstCodeService mstCodeService,
      MstMatterRepository mstMatterRepository,
      MstSupplierManagementService mstSupplierManagementService,
      MstCodeRepository mstCodeRepesitory, CommonService commonService) {
    this.mstAcceptingOrderRepository = mstAcceptingOrderRepository;
    this.mstAcceptingOrderDetailRepository = mstAcceptingOrderDetailRepository;
    this.mstAcceptingOrderHistoryRepository = mstAcceptingOrderHistoryRepository;
    this.mstCaseRepository = mstCaseRepository;
    this.mstCodeService = mstCodeService;
    this.mstMatterRepository = mstMatterRepository;
    this.mstSupplierManagementService = mstSupplierManagementService;
    this.mstCodeRepesitory = mstCodeRepesitory;
    this.commonService = commonService;
  }

  public List<MstAcceptingOrder> findDisplay() {
    List<MstAcceptingOrder> OrderInfoList = mstAcceptingOrderRepository.findAll();
    return OrderInfoList;
  }

  public List<MstMatter> findCaseDisplay() {
    List<MstMatter> CaseInfoList = mstCaseRepository.findAll();
    return CaseInfoList;
  }

  // 発注一覧表示
  public List<MstAcceptingOrder> getAllOrder(int id) {
    List<MstAcceptingOrder> acception = mstAcceptingOrderRepository.getAcceptingOrder(id);
    return acception;
  }

  public List<MstAcceptingOrderForm> viewAcceptionOrderForm(int id) {
    List<MstAcceptingOrder> acception = getAllOrder(id);
    List<MstAcceptingOrderForm> mstOrderForm = new ArrayList<MstAcceptingOrderForm>();
    for (MstAcceptingOrder order : acception) {
      mstOrderForm.add(EntityToForm(order));
    }
    return mstOrderForm;
  }

  public MstAcceptingOrderForm EntityToForm(MstAcceptingOrder mstAcceptingOrder) {
    MstAcceptingOrderForm tmp = new MstAcceptingOrderForm();
    tmp.setId(mstAcceptingOrder.getId().toString());
    tmp.setMatterId(mstAcceptingOrder.getMatterId().toString());
    tmp.setEstimateId(mstAcceptingOrder.getEstimateItemId().toString());
    tmp.setSuppliermanagementId(mstAcceptingOrder.getSuppliermanagerId().toString());
    tmp.setSuppliermanagementName(
        mstSupplierManagementService.getSuppliereById(mstAcceptingOrder.getId()).getVenderName());
    MstCode mstCode = mstCodeRepesitory
        .findByCodeKindAndBranchNum(ORDER_STATUS, mstAcceptingOrder.getOrderStatus())
        .orElse(new MstCode());
    tmp.setOrderStatus(mstCode.getCodeName());
    tmp.setConStartDate(mstAcceptingOrder.getStartDate());
    tmp.setConEndDate(mstAcceptingOrder.getEndDate());
    tmp.setSubtotal(mstAcceptingOrder.getSubtotal().toString());
    tmp.setConsumptionTax(mstAcceptingOrder.getConsumptionTax().toString());
    tmp.setTotal(mstAcceptingOrder.getTotal().toString());
    tmp.setKeyLocation(mstAcceptingOrder.getKeyLocation());
    tmp.setRegisteredUserId(this.commonService.getLoginUserId());
    tmp.setRegistrationDatetime(mstAcceptingOrder.getRegistrationDatetime());
    tmp.setLastUpdatedDatetime(mstAcceptingOrder.getLastUpdatedDatetime());
    return tmp;
  }

  // 発注詳細表示
  public List<MstAcceptingOrderDetail> getAllOrderDetail(int id) {
    List<MstAcceptingOrderDetail> detail =
        mstAcceptingOrderDetailRepository.getAcceptingOrderDetail(id);
    return detail;
  }

  public List<MstAcceptingOrderDetailForm> viewAcceptionOrderDetailForm(int id) {
    List<MstAcceptingOrderDetail> detail = getAllOrderDetail(id);
    List<MstAcceptingOrderDetailForm> mstOrderDetailForm =
        new ArrayList<MstAcceptingOrderDetailForm>();
    for (MstAcceptingOrderDetail order : detail) {
      mstOrderDetailForm.add(EntityToDetailForm(order));
    }
    return mstOrderDetailForm;
  }

  public MstAcceptingOrderDetailForm EntityToDetailForm(
      MstAcceptingOrderDetail mstAcceptingOrderDetail) {
    MstAcceptingOrderDetailForm tmp = new MstAcceptingOrderDetailForm();
    tmp.setId(mstAcceptingOrderDetail.getId());
    tmp.setMatterId(mstAcceptingOrderDetail.getMatterId().toString());
    tmp.setOrderId(mstAcceptingOrderDetail.getOrderId().toString());
    tmp.setEstimateItemId(mstAcceptingOrderDetail.getEstimateId().toString());
    tmp.setContent(mstAcceptingOrderDetail.getContent());
    tmp.setManufacturerName(mstAcceptingOrderDetail.getManufacturerName());
    tmp.setModelNumber(mstAcceptingOrderDetail.getModelNumber());
    MstCode mstCode = mstCodeRepesitory
        .findByCodeKindAndBranchNum(UNIT, mstAcceptingOrderDetail.getUnit()).orElse(new MstCode());
    tmp.setUnit(mstCode.getCodeName());
    tmp.setUnitPrice(mstAcceptingOrderDetail.getUnitPrice().toString());
    tmp.setVolume(mstAcceptingOrderDetail.getVolume().toString());
    tmp.setSubtotal(mstAcceptingOrderDetail.getSubtotal().toString());
    tmp.setNote(mstAcceptingOrderDetail.getNote());
    tmp.setRegisteredUserId(this.commonService.getLoginUserId());
    tmp.setRegistrationDatetime(mstAcceptingOrderDetail.getRegistrationDatetime());
    tmp.setLastUpdatedDatetime(mstAcceptingOrderDetail.getLastUpdatedDatetime());
    return tmp;
  }

  public List<MstAcceptingOrderForm> viewOrderForm() {
    List<MstAcceptingOrder> mstOrder = findDisplay();
    List<MstAcceptingOrderForm> mstOrderForm = new ArrayList<MstAcceptingOrderForm>();
    for (MstAcceptingOrder orderNum : mstOrder) {
      mstOrderForm.add(updateOrderForm(orderNum));
    }
    return mstOrderForm;
  }

  public List<MstMatterForm> viewCaseForm() {
    List<MstMatter> mstCase = findCaseDisplay();
    List<MstMatterForm> mstCaseForm = new ArrayList<MstMatterForm>();
    MstCode code = mstCodeService.getCodeByKindAndName("受注", SITUATION_STATUS);
    for (MstMatter caseNum : mstCase) {
      if (caseNum.getSituationStatus() == code.getCodeBranchNum())
        mstCaseForm.add(updateCaseForm(caseNum));
    }

    return mstCaseForm;

  }

  public List<MstMatterForm> selectMatterBySituationStatus(List<MstMatterForm> matterFormList) {
    List<MstMatterForm> mstCaseForm = new ArrayList<MstMatterForm>();
    MstCode code = mstCodeService.getCodeByKindAndName("受注", SITUATION_STATUS);
    for (MstMatterForm caseNum : matterFormList) {
      if (caseNum.getSituationStatus().equals(code.getCodeName()))
        mstCaseForm.add(caseNum);
    }
    return mstCaseForm;
  }


  public MstAcceptingOrderForm updateOrderForm(MstAcceptingOrder mstOrder) {
    MstAcceptingOrderForm tmp = new MstAcceptingOrderForm();

    // MstCode mstCode = mstCodeService.getCodeByKindAndBranch(TASK_SUBSTANCE,
    // mstCase.getCaseKind());

    // tmp.setId(String.valueOf(mstOrder.getId()));
    // tmp.setMatterId(String.valueOf(mstOrder.getMatterId()));
    // tmp.setConStartDate(mstOrder.getConStartDate());
    // tmp.setConEndDate(mstOrder.getConEndDate());

    return tmp;
  }

  public MstMatterForm updateCaseForm(MstMatter mstMatter) {

    MstMatterForm tmp = new MstMatterForm();

    MstCode mstCode =
        mstCodeService.getCodeByKindAndBranch(TASK_SUBSTANCE, mstMatter.getTaskSubstance());

    tmp.setTaskSubstance(mstCode.getCodeName());
    tmp.setMatterName(mstMatter.getMatterName());
    tmp.setCustomerName(mstMatter.getCustomerName());
    tmp.setPropertyName(mstMatter.getPropertyName());
    // tmp.setCaseTenantAddress(mstMatter.getCaseTenantAddress());
    // tmp.setCaseTenantBuilding(mstMatter.getCaseTenantBuilding());
    // tmp.setCaseTenantName(mstMatter.getCaseTenantName());
    tmp.setScheduledVisitDatetime(mstMatter.getScheduledVisitDatetime());
    tmp.setRegistrationDatetime(mstMatter.getRegistrationDatetime());
    tmp.setUpdateDatetime(mstMatter.getUpdateDatetime());
    // tmp.setCaseTenantBranchName(mstMatter.getCaseTenantBranchName());

    return tmp;
  }

  public List<MstMatterForm> search(MstMatterForm mstMatterForm) {
    String situationStatus = "";
    String taskSubstance = "";
    String visit = "";
    String matterName = "";
    String customerName = "";
    String propertyName = "";
    String propertyAddress = "";
    String propertyBuildingName = "";
    String facility = "";
    String createdAt1 = "";
    String updatedAt1 = "";

    MstMatterForm matter = mstMatterForm;
    if (matter.getSituationStatus() == null || matter.getSituationStatus().equals("")) {
      situationStatus += "";
    } else {
      MstCode mstCode =
          mstCodeService.getCodeByKindAndName(matter.getSituationStatus(), SITUATION_STATUS);
      matter.setSituationStatus(mstCode.getCodeBranchNum().toString());
      situationStatus += matter.getSituationStatus();
    }
    if (matter.getTaskSubstance() == null || matter.getTaskSubstance().equals("")) {
      taskSubstance += "";
    } else {
      MstCode mstCode =
          mstCodeService.getCodeByKindAndName(matter.getTaskSubstance(), TASK_SUBSTANCE);
      matter.setTaskSubstance(mstCode.getCodeBranchNum().toString());
      taskSubstance += matter.getTaskSubstance();
    }
    visit += nullCheck(matter.getVisit());
    matterName += nullCheck(matter.getMatterName());
    customerName += nullCheck(matter.getCustomerName());
    propertyName += nullCheck(matter.getPropertyName());
    propertyAddress += nullCheck(matter.getPropertyAddress());
    propertyBuildingName += nullCheck(matter.getPropertyBuildingName());
    if (matter.getFacility() == null || matter.getFacility().equals("")) {
      facility += "";
    } else {
      if (matter.getFacility().equals("有")) {
        facility = "1";
      } else {
        facility = "0";
      }
    }
    createdAt1 += nullCheck(matter.getCreatedAt1());
    updatedAt1 += nullCheck(matter.getUpdatedAt1());

    List<MstMatter> a =
        mstMatterRepository.search(situationStatus, taskSubstance, visit, matterName, customerName,
            propertyName, propertyAddress, propertyBuildingName, facility, createdAt1, updatedAt1);

    List<MstMatterForm> mstCaseFormList = new ArrayList<>();
    for (MstMatter case3 : a) {
      mstCaseFormList.add(updateCaseForm(case3));
    }
    return mstCaseFormList;

  }

  // 発注
  public MstAcceptingOrderForm showAcceptingOrderForm() {
    MstAcceptingOrderForm tmp = new MstAcceptingOrderForm();
    return tmp;
  }

  public MstAcceptingOrderDetailForm showAcceptingOrderDetailForm() {
    logger.info("--- MstAcceptingOrderService.showAcceptingOrderDetailForm START ---");

    MstAcceptingOrderDetailForm tmp = new MstAcceptingOrderDetailForm();

    logger.info("--- MstAcceptingOrderService.showAcceptingOrderDetailForm END ---");
    return tmp;
  }

  public List<MstAcceptingOrderForm> orderSearch(MstAcceptingOrderForm mstAcceptingOrderForm,
      Integer id) {
    String orderStatus = "";
    String suppliermanagementName = "";
    String cStart = "";
    String cEnd = "";

    MstAcceptingOrderForm order = mstAcceptingOrderForm;
    if (order.getOrderStatus() == null || order.getOrderStatus().equals("")) {
      orderStatus += "";
    } else {
      MstCode mstCode = mstCodeService.getCodeByKindAndName(order.getOrderStatus(), ORDER_STATUS);
      order.setOrderStatus(mstCode.getCodeBranchNum().toString());
      orderStatus += order.getOrderStatus();
    }
    suppliermanagementName += nullCheck(order.getSuppliermanagementName());
    cStart += nullCheck(order.getCStart());
    cEnd += nullCheck(order.getCEnd());

    List<MstAcceptingOrder> a =
        mstAcceptingOrderRepository.search(orderStatus, suppliermanagementName, cStart, cEnd, id);

    List<MstAcceptingOrderForm> orderFormList = new ArrayList<MstAcceptingOrderForm>();
    for (MstAcceptingOrder order1 : a) {
      orderFormList.add(EntityToForm(order1));
    }
    return orderFormList;

  }

  public LocalDateTime getEarliestOrderStartDates(int i) {
    List<Object[]> results = mstAcceptingOrderRepository.findOrderStartDateByMatter();
    LocalDateTime earliestDates = null;
    for (Object[] result : results) {
      // Timestamp を LocalDate に変換する
      if (i == (int) result[0]) {
        Timestamp timestamp = (Timestamp) result[1];
        LocalDateTime earliestDate = timestamp.toLocalDateTime();
        earliestDates = earliestDate;
      }
    }
    return earliestDates;
  }

  public LocalDateTime getLattermostOrderEndDates(int i) {
    List<Object[]> results = mstAcceptingOrderRepository.findOrderEndDateByMatter();
    LocalDateTime lattermostDates = null;
    for (Object[] result : results) {
      // Timestamp を LocalDate に変換する
      if (i == (int) result[0]) {
        Timestamp timestamp = (Timestamp) result[1];
        LocalDateTime lattermostDate = timestamp.toLocalDateTime();
        lattermostDates = lattermostDate;
      }

    }
    return lattermostDates;
  }

  public void orderStatus(int orderId, int status) {
    Optional<MstAcceptingOrder> order = mstAcceptingOrderRepository.findById(orderId);
    if (order.isPresent()) {
      order.get().setOrderStatus(status);
      order.get().setLastUpdatedDatetime(LocalDateTime.now());
      mstAcceptingOrderRepository.save(order.get());
    } else {
      throw new EntityNotFoundException("Order not found");
    }
  }

  public void addOrderHistory(int orderId, String updateContent) {
    MstAcceptingOrderHistory history = new MstAcceptingOrderHistory();
    history.setOrderId(orderId);
    history.setNote(updateContent);
    history.setRegistrationDatetime(LocalDateTime.now()); // 現在時刻を設定

    try {
      mstAcceptingOrderHistoryRepository.save(history); // データベースに保存
    } catch (Exception e) {
      // エラーログを出力
      e.printStackTrace();
      throw new RuntimeException("Failed to add order history: " + e.getMessage());
    }
  }
}
