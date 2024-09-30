package com.seproject.buildmanager.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.common.ExcelFileService;
import com.seproject.buildmanager.entity.MstAuth;
import com.seproject.buildmanager.entity.MstAuthType;
import com.seproject.buildmanager.form.MstAuthForm;
import com.seproject.buildmanager.repository.MstAuthRepository;
import com.seproject.buildmanager.repository.MstAuthTypeRepository;

/**
 * 権限に関するビジネスロジックを提供するサービスクラスです。
 * 
 * <p>
 * このクラスは、権限のCRUD操作およびその他のビジネスロジックを処理します。
 * 
 * <p>
 * 変更履歴：
 * <ul>
 * <li>2024/10/31 - 初版作成</li>
 * </ul>
 * 
 * @since 1.0
 * @version 1.0
 */
@Service
public class MstAuthService implements MstSearchService<MstAuthForm, MstAuth> {


  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstAuthRepository mstAuthRepository;

  private final MstAuthTypeRepository mstAuthTypeRepository;

  private final ExcelFileService<MstAuth> excelFileService;

  private final CommonService commonService;

  /**
   * コンストラクタ。
   * 
   * @param mstAuthRepository 権限リポジトリ
   * @param mstAuthTypeRepository 権限タイプリポジトリ
   */
  public MstAuthService(MstAuthRepository mstAuthRepository,
      MstAuthTypeRepository mstAuthTypeRepository, ExcelFileService<MstAuth> excelFileService,
      CommonService commonService) {
    this.mstAuthRepository = mstAuthRepository;
    this.mstAuthTypeRepository = mstAuthTypeRepository;
    this.excelFileService = excelFileService;
    this.excelFileService.initializeExcel("権限管理.xlsx");
    this.commonService = commonService;

  }

  public MstAuthForm mstAuthForm() {
    MstAuthForm mstAuthForm = new MstAuthForm();
    return mstAuthForm;
  }


  /**
   * 全ての権限を取得します。
   * 
   * @return 権限のリスト
   */
  public List<MstAuth> getAllAuth() {
    logger.info("--- MstAuthService.getAllAuth START ---");

    List<MstAuth> auths = this.mstAuthRepository.findAll();

    logger.info("--- MstAuthService.getAllAuth END ---");

    return auths;
  }

  public List<MstAuthType> getAllAuthType() {
    logger.info("--- MstAuthService.getAllAuth START ---");

    List<MstAuthType> authTypes = this.mstAuthTypeRepository.findAll();

    logger.info("--- MstAuthService.getAllAuth END ---");

    return authTypes;
  }

  /**
   * 全ての有効な権限を取得します。
   * 
   * @return 権限のリスト
   */
  public List<MstAuth> getAllActiveAuth() {
    logger.info("--- MstAuthService.getAllActiveAuth START ---");

    List<MstAuth> auths = this.mstAuthRepository.findByStatus(Constants.STATUS_TRUE);

    logger.info("--- MstAuthService.getAllActiveAuth END ---");

    return auths;
  }

  /**
   * 指定されたIDに対応する権限を取得します。
   * 
   * @param id 権限ID
   * @return 指定されたIDに対応するMstAuthオブジェクト
   */
  public MstAuth findByIdA(Integer id) {
    logger.info("--- MstAuthService.findByIdA START ---");

    MstAuth mstAuth = this.mstAuthRepository.findById(id);

    logger.info("--- MstAuthService.findByIdA END ---");

    return mstAuth;
  }

  /**
   * 指定されたIDに対応する権限名を取得します。
   * 
   * @param id 権限タイプID
   * @return 指定されたIDに対応する権限名
   */
  public String getAuthNameById(Integer id) {
    logger.info("--- MstAuthService.getAuthNameById START ---");

    MstAuthType mstAuthType = this.mstAuthTypeRepository.findById(id).orElse(null);
    String authName = mstAuthType != null ? mstAuthType.getAuthName() : null;

    logger.info("--- MstAuthService.getAuthNameById END ---");

    return authName;
  }

  public MstAuth saveAuthRegister(String name, Integer id) {// 登録

    logger.info("--- MstOwnerService.saveUser START ---");

    MstAuth tmp = new MstAuth();
    tmp.setMstAuthTypeId(id);
    tmp.setName(name);
    tmp.setCreatedAt(LocalDateTime.now());
    tmp.setUpdatedAt(LocalDateTime.now());
    tmp.setUpdatedMstUserId(this.commonService.getLoginUserId());
    tmp.setStatus(1);

    MstAuth result = mstAuthRepository.save(tmp);

    logger.info("--- MstOwnerService.saveUser END ---");
    return result;

  }

  public MstAuth saveAuthUpdate(String name, Integer authTypeId, Integer id) {
    MstAuth tmp = mstAuthRepository.findById(id);
    tmp.setMstAuthTypeId(authTypeId);
    tmp.setName(name);
    tmp.setUpdatedAt(LocalDateTime.now());

    MstAuth result = mstAuthRepository.save(tmp);

    logger.info("--- MstOwnerService.saveUser END ---");
    return result;
  }

  @Override
  public List<MstAuth> search(MstAuthForm mstAuthForm) {
    String mstauthtypeid = "";
    String name = "";
    String status = "";
    String createdAt = "";
    String updatedAt = "";


    MstAuthForm auth = mstAuthForm;
    if (auth.getMstauthtypeid() == null || auth.getMstauthtypeid().equals("")) {
      mstauthtypeid += "";
    } else {
      mstauthtypeid += auth.getMstauthtypeid();
    }
    name += nullCheck(auth.getName());
    if (auth.getStatus() == null || auth.getStatus().equals("")) {
      status += "";
    } else {
      if (auth.getStatus().equals("active")) {
        status += "1";
      } else {
        status += "0";
      }
    }
    createdAt += nullCheck(auth.getCreatedAt1());
    updatedAt += nullCheck(auth.getUpdatedAt1());



    List<MstAuth> a = mstAuthRepository.search(name, mstauthtypeid, status, createdAt, updatedAt);


    return a;
  }

  public void download() {
    try {
      this.excelFileService.exportDataTypeExcel(getAllAuth(), "Auth");
    } catch (IOException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }

  public void upload() {
    this.excelFileService.setSheetName("users");
    int a = this.excelFileService.getRowNum();
    for (int i = 1; i <= a; i++) {
      mstAuthRepository
          .save(this.excelFileService.importDataToRowTypeExcel(new MstAuth(), "auth", i));
    }
  }

  public void toggleStatus(Integer authId) {
    mstAuthRepository.toggleStatus(authId);
  }
}
