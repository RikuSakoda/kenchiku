package com.seproject.buildmanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.MstCodeEnums;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.entity.MstEstimateItem;
import com.seproject.buildmanager.entity.MstEstimateManagement;
import com.seproject.buildmanager.entity.MstMatter;
import com.seproject.buildmanager.entity.MstUser;
import com.seproject.buildmanager.form.MstEstimateManagementForm;
import com.seproject.buildmanager.repository.MstCodeRepository;
import com.seproject.buildmanager.repository.MstEstimateItemRepository;
import com.seproject.buildmanager.repository.MstEstimateManagementRepository;
import com.seproject.buildmanager.repository.MstMatterRepository;
import jakarta.mail.MessagingException;

@Service

public class MstEstimateManagementService {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstEstimateManagementRepository mstEstimateManagementRepository;

  private final MstEstimateItemRepository mstEstimateItemRepository;

  private final MstMatterRepository mstMatterRepository;

  private final MstCodeRepository mstCodeRepesitory;

  private final MstMatterService mstMatterService;

  private final MstCodeService mstCodeService;

  private final EmailService emailService;

  private final CommonService commonService;

  private final MstUserService mstUserService;

  public MstEstimateManagementService(
      MstEstimateManagementRepository mstEstimateManagementRepository,
      MstEstimateItemRepository mstEstimateItemRepository, MstCodeRepository mstCodeRepesitory,
      MstMatterRepository mstMatterRepository, MstMatterService mstMatterService,
      MstCodeService mstCodeService, EmailService emailService, CommonService commonService,
      MstUserService mstUserService) {
    this.mstEstimateManagementRepository = mstEstimateManagementRepository;
    this.mstEstimateItemRepository = mstEstimateItemRepository;
    this.mstCodeRepesitory = mstCodeRepesitory;
    this.mstMatterRepository = mstMatterRepository;
    this.mstMatterService = mstMatterService;
    this.mstCodeService = mstCodeService;
    this.emailService = emailService;
    this.commonService = commonService;
    this.mstUserService = mstUserService;
  }

  private static final int SITUATION_STATUS = MstCodeEnums.SITUATION_STATUS.getValue();

  // 都道府県取得
  private static final int PREFECTURES = MstCodeEnums.PREFECTURES.getValue();
  // Enumから種別を取得するためののcode_kindの値を取得
  private static final int TASK_SUBSTANCE = MstCodeEnums.TASK_SUBSTANCE.getValue();

  private static final int ACCEPTING_ORDERS_STATUS =
      MstCodeEnums.ACCEPTING_ORDERS_STATUS.getValue();


  public List<MstEstimateManagement> findAll() {
    return mstEstimateManagementRepository.getEstimate();
  }

  public MstEstimateManagement getEstimateId(int id) {// id検索
    return mstEstimateManagementRepository.findById(id).orElse(new MstEstimateManagement());
  }


  public List<MstEstimateManagementForm> findByDisply() {

    List<MstEstimateManagement> mstEstimate = findAll();
    List<MstEstimateManagement> mst = new ArrayList<MstEstimateManagement>();
    for (MstEstimateManagement m : mstEstimate) {
      m.setMatter(
          mstMatterRepository.findDisplayById(m.getMatter().getId()).orElse(new MstMatter()));
      mst.add(m);
    }
    List<MstEstimateManagementForm> mstEstimateForm = new ArrayList<MstEstimateManagementForm>();
    for (MstEstimateManagement Estimate : mst) {
      mstEstimateForm.add(updateCaseForm(Estimate));
    }
    List<MstEstimateManagementForm> mstForm = new ArrayList<MstEstimateManagementForm>();
    for (MstEstimateManagementForm Estimate : mstEstimateForm) {
      if (Estimate.getMstMatterForm().getSituationStatus().equals("現地調査・退去立会後")) {
        mstForm.add(Estimate);
      }
    }
    return mstForm;
  }

  public List<MstEstimateManagement> findByMatterId(Integer matterId) {
    return mstEstimateManagementRepository.getEstimateByMatter(matterId);
  }

  // 表示用にエンティティから取ってきたデータをフォームに格納する
  public MstEstimateManagementForm updateCaseForm(MstEstimateManagement mstEstimateManagement) {
    MstEstimateManagementForm tmp = new MstEstimateManagementForm();

    tmp.setId(mstEstimateManagement.getId());
    tmp.setEstimateVersion(mstEstimateManagement.getEstimateVersion().toString());
    MstCode mstCode = mstCodeRepesitory.findByCodeKindAndBranchNum(ACCEPTING_ORDERS_STATUS,
        mstEstimateManagement.getAcceptionOrderStatus()).orElse(new MstCode());
    tmp.setAcceptionOrderStatus(mstCode.getCodeName());
    tmp.setAcceptionOrderStatusNum(mstEstimateManagement.getAcceptionOrderStatus());
    tmp.setEstimateSubtotal(mstEstimateManagement.getEstimateSubtotal().toString());
    tmp.setEstimateConsumptionTax(mstEstimateManagement.getEstimateConsumptionTax().toString());
    tmp.setEstimateTotal(mstEstimateManagement.getEstimateTotal().toString());
    tmp.setApprovalSubtotal(mstEstimateManagement.getApprovalSubtotal().toString());
    tmp.setApprovalConsumptionTax(mstEstimateManagement.getApprovalConsumptionTax().toString());
    tmp.setApprovalTotal(mstEstimateManagement.getApprovalTotal().toString());
    tmp.setMemo(mstEstimateManagement.getMemo());
    tmp.setRegistrationDatetime(mstEstimateManagement.getRegistrationDatetime());
    tmp.setLatestUpdateDatetime(mstEstimateManagement.getLatestUpdateDatetime());
    tmp.setMstMatterForm(mstMatterService.updateCaseForm(mstEstimateManagement.getMatter()));
    return tmp;
  }

  public MstEstimateManagement getEstimateByMatterIdAndVarsion(Integer matterId, Integer varsion) {
    return this.mstEstimateManagementRepository.getEstimateByMatterAndVarsion(matterId, varsion)
        .orElse(null);
  }

  public MstEstimateManagement saveNewEstimate(Integer matterId, List<MstEstimateItem> items) {


    MstEstimateManagement mstEstimateManagement = new MstEstimateManagement();
    mstEstimateManagement.setId(null);
    mstEstimateManagement.setMatter(mstMatterService.findDisplayById(matterId));
    mstEstimateManagement.setEstimateVersion(this.newestVarsion(matterId) + 1);

    int subtotal = 0;
    int approvalSubtotal = 0;
    String memo = "";
    for (MstEstimateItem item : items) {
      if (item.getEstimateUnitPrice() != null) {
        subtotal += item.getEstimateUnitPrice();
      }
      if (item.getApprovalUnitPrice() != null) {
        approvalSubtotal += item.getApprovalUnitPrice();
      }
      if (item.getNote() != null) {
        memo += item.getNote() + " ";
      }
    }

    mstEstimateManagement.setMemo(memo);

    int consumptionTax = (int) (subtotal * 0.1);
    int approvalConsumptionTax = (int) (approvalSubtotal * 0.1);
    int total = subtotal + consumptionTax;
    int approvalTotal = approvalSubtotal + approvalConsumptionTax;

    mstEstimateManagement.setEstimateSubtotal(subtotal);
    mstEstimateManagement.setEstimateConsumptionTax(consumptionTax);
    mstEstimateManagement.setEstimateTotal(total);
    mstEstimateManagement.setApprovalSubtotal(approvalSubtotal);
    mstEstimateManagement.setApprovalConsumptionTax(approvalConsumptionTax);
    mstEstimateManagement.setApprovalTotal(approvalTotal);

    mstEstimateManagement.setRegistrationDatetime(LocalDateTime.now());
    mstEstimateManagement.setLatestUpdateDatetime(LocalDateTime.now());


    return mstEstimateManagementRepository.save(mstEstimateManagement);
  }

  public Integer newestVarsion(Integer matterId) {
    List<MstEstimateManagement> estimate =
        mstEstimateManagementRepository.getEstimateByMatter(matterId);
    Integer ver = 0;
    for (MstEstimateManagement m : estimate) {
      if (ver < m.getEstimateVersion()) {
        ver = m.getEstimateVersion();
      }
    }
    return ver;
  }

  public Map<String, String> mekeEstimateTotals() {
    Map<String, String> totals = new LinkedHashMap<String, String>();
    totals.put("見積用小計", "subtotal");
    totals.put("見積用消費税", "tax");
    totals.put("見積用合計", "total");
    totals.put("原状回復工事費用承諾書用小計", "approvalSubtotal");
    totals.put("原状回復工事費用承諾書用消費税", "approvalTax");
    totals.put("原状回復工事費用承諾書用合計", "approvalTotal");
    return totals;
  }

  public String detailSheetUrl(Integer matterId) {
    MstCode situationStatus =
        this.mstCodeService.getCodeByKindAndName("現地調査・退去立会後", SITUATION_STATUS);
    if (this.mstMatterService.findDisplayById(matterId).getSituationStatus() == situationStatus
        .getCodeBranchNum()) {
      return "/js/estimate/detailSheetRecessionText.js";
    } else {
      return "/js/estimate/detailSheetRepair.js";
    }
  }

  public void sendMail(String matterName) {
    Map<String, Object> variables = new HashMap<>();
    MstUser testUser = this.mstUserService.getUserById(this.commonService.getLoginUserId());
    variables.put("name", this.commonService.getLoginUserName());
    variables.put("matterName", matterName);

    try { // 承認依頼メールをHTML形式で送信
      emailService.sendHtmlEmail(testUser.getEmail(), "承認依頼", variables, "/request.txt", null); // ファイル名修正
    } catch (MessagingException e) {
      e.printStackTrace(); // 例外処理
    }

  }

}
