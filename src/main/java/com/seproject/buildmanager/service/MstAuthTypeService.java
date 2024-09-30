package com.seproject.buildmanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.entity.MstAuthType;
import com.seproject.buildmanager.form.MstAuthTypeForm;
import com.seproject.buildmanager.repository.MstAuthTypeRepository;

@Service
public class MstAuthTypeService implements MstSearchService<MstAuthTypeForm, MstAuthTypeForm> {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstAuthTypeRepository mstAuthTypeRepository;

  private final CommonService commonService;

  private MstAuthTypeService(MstAuthTypeRepository mstAuthTypeRepository,
      CommonService commonService) {
    this.mstAuthTypeRepository = mstAuthTypeRepository;
    this.commonService = commonService;
  }

  public MstAuthTypeForm mstAuthTypeForm() {
    MstAuthTypeForm mstAuthTypeForm = new MstAuthTypeForm();
    return mstAuthTypeForm;
  }

  public MstAuthType saveAuthTypeRegister(String name) {// 登録

    logger.info("--- MstConstructionService.saveConstructionRegister START ---");



    MstAuthType tmp = new MstAuthType();
    tmp.setAuthName(name);

    try {
      tmp.setStatus(Constants.STATUS_TRUE);
    } catch (NumberFormatException e) {
      throw new RuntimeException("ステータスが正常ではありません", e);
    }
    tmp.setCreatedAt(LocalDateTime.now());
    tmp.setUpdatedMstUserId(this.commonService.getLoginUserId());
    tmp.setUpdatedAt(null);

    MstAuthType result = mstAuthTypeRepository.save(tmp);

    logger.info("--- MstConstructionService.saveConstructionRegister END ---");
    return result;

  }

  public List<MstAuthType> getAllAuthType() {

    logger.info("--- MstCheckGroupeService.getAllGroupes START ---");

    List<MstAuthType> authtype = mstAuthTypeRepository.findAll();

    logger.info("--- MstUserService.getAllGroupes END ---");

    return authtype;
  }

  public MstAuthType saveAuthTypeUpdate(String name, Integer id) {// 変更

    logger.info("--- MstConstructionService.saveUser START ---");
    MstAuthType a = mstAuthTypeRepository.findById(id).orElse(new MstAuthType());
    MstAuthType tmp = new MstAuthType();
    tmp.setId(id);
    tmp.setAuthName(name);
    try {
      tmp.setStatus(a.getStatus());
    } catch (NumberFormatException e) {
      throw new RuntimeException("ステータスが正常ではありません", e);
    }
    tmp.setCreatedAt(a.getCreatedAt());
    tmp.setUpdatedAt(LocalDateTime.now());
    tmp.setUpdatedMstUserId(this.commonService.getLoginUserId());
    // tmp.setUpdate_user_id(this.commonService.getLoginUserId());// userのidがわからないため保留
    MstAuthType update = mstAuthTypeRepository.save(tmp);

    logger.info("--- MstConstructionService.saveUser END ---");
    return update;

  }

  // 検索
  @Override
  public List<MstAuthTypeForm> search(MstAuthTypeForm mstAuthTypeForm) {
    String authName = "";
    String createdAt = "";
    String updatedAt = "";
    String status = "";

    MstAuthTypeForm authtype = mstAuthTypeForm;

    authName += this.nullCheck(authtype.getAuthName());
    if (authtype.getStatus() == null || authtype.getStatus().equals("")) {
      status += "";
    } else {
      if (authtype.getStatus().equals("active")) {
        status += "1";
      } else {
        status += "0";
      }
    }
    createdAt += this.nullCheck(authtype.getCreatedAt1());
    updatedAt += this.nullCheck(authtype.getUpdatedAt1());



    List<MstAuthType> a = mstAuthTypeRepository.search(authName, status, createdAt, updatedAt);
    List<MstAuthTypeForm> mstAuthTypeFormList = new ArrayList<>();
    for (MstAuthType authtype1 : a) {
      mstAuthTypeFormList.add(updateAuthTypeForm(authtype1.getId()));
    }
    return mstAuthTypeFormList;
  }

  @Override
  public String nullCheck(String field) {
    String key = "";
    if (!(field == null || field.equals(""))) {
      key += "%" + field + "%";
    }
    return key;
  }

  public MstAuthTypeForm updateAuthTypeForm(Integer id) {
    MstAuthType mstAuthType = mstAuthTypeRepository.findById(id).orElse(new MstAuthType());
    MstAuthTypeForm tmp = new MstAuthTypeForm();
    tmp.setId(mstAuthType.getId());
    tmp.setAuthName(mstAuthType.getAuthName());
    if (mstAuthType.getStatus() != null) {
      tmp.setStatus(mstAuthType.getStatus().toString());
    } else {
      tmp.setStatus(null);
    }
    tmp.setCreatedAt(mstAuthType.getCreatedAt());
    tmp.setUpdatedAt(mstAuthType.getUpdatedAt());
    tmp.setUpdatedMstUserId(mstAuthType.getUpdatedMstUserId());
    return tmp;
  }
}
