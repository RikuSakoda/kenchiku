package com.seproject.buildmanager.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.common.ExcelFileService;
import com.seproject.buildmanager.entity.MstCheckChange;
import com.seproject.buildmanager.entity.MstCheckChangeRegistration;
import com.seproject.buildmanager.form.MstCheckChangeForm;
import com.seproject.buildmanager.repository.MstCheckChangeRegistrationRepositry;
import com.seproject.buildmanager.repository.MstCheckChangeRepository;

/**
 * チェックグループ管理に関するビジネスロジックを提供するサービスクラスです。
 * 
 * <p>
 * このクラスは、チェックグループのCRUD操作およびその他のビジネスロジックを処理します。
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
public class MstCheckChangeService
    implements MstSearchService<MstCheckChangeForm, MstCheckChangeRegistration> {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstCheckChangeRegistrationRepositry mstCheckChangeRegistrationRepositry;

  private final MstCheckChangeRepository mstCheckChangeRepository;

  private final CommonService commonService;

  private final ExcelFileService<MstCheckChangeRegistration> excelFileService;


  /**
   * コンストラクタ。
   * 
   * @param mstCheckChangeRegistrationRepositry チェックグループ登録リポジトリ
   * @param commonService 共通サービス
   * @param mstCheckChangeRepository チェックグループ変更リポジトリ
   */
  public MstCheckChangeService(
      MstCheckChangeRegistrationRepositry mstCheckChangeRegistrationRepositry,
      CommonService commonService, MstCheckChangeRepository mstCheckChangeRepository,
      ExcelFileService<MstCheckChangeRegistration> excelFileService) {
    this.mstCheckChangeRegistrationRepositry = mstCheckChangeRegistrationRepositry;
    this.commonService = commonService;
    this.mstCheckChangeRepository = mstCheckChangeRepository;
    this.excelFileService = excelFileService;
    this.excelFileService.initializeExcel("チェック項目グループ登録変更.xlsx");
  }

  /**
   * 指定されたIDのチェックグループをフォーム形式で取得します。
   * 
   * @param id チェックグループID
   * @return チェックグループフォーム
   */
  public MstCheckChangeForm updateCheckChangeForm(Integer id) {
    MstCheckChangeRegistration mstCheckChange =
        mstCheckChangeRegistrationRepositry.findById(id).orElse(new MstCheckChangeRegistration());
    MstCheckChangeForm tmp = new MstCheckChangeForm();
    tmp.setId((mstCheckChange.getId()));
    tmp.setCheckGroupName(mstCheckChange.getCheckGroupName());
    tmp.setStatus(String.valueOf(mstCheckChange.getStatus()));
    tmp.setCreatedAt(mstCheckChange.getCreatedAt());
    tmp.setUpdatedAt(mstCheckChange.getUpdatedAt());
    tmp.setUpdatedMstUserId(mstCheckChange.getUpdatedMstUserId());


    return tmp;

  }

  /**
   * 新規チェックグループフォームを作成します。
   * 
   * @return 新規チェックグループフォーム
   */
  public MstCheckChangeForm mstCheckChangeForm() {
    MstCheckChangeForm tmp = new MstCheckChangeForm();
    return tmp;
  }



  /**
   * チェックグループを登録します。
   * 
   * @param name チェックグループ名
   * @return 登録されたチェックグループ
   */
  @Transactional(rollbackFor = Exception.class)
  public MstCheckChangeRegistration saveCheckChangeRegister(String name) {// 登録

    logger.info("--- MstCheckGroupService.saveCheckGroupRegister START ---");

    MstCheckChangeRegistration tmp = new MstCheckChangeRegistration();
    tmp.setCheckGroupName(name);
    tmp.setStatus(Constants.STATUS_TRUE);
    tmp.setUpdatedMstUserId(this.commonService.getLoginUserId());

    MstCheckChangeRegistration result = mstCheckChangeRegistrationRepositry.save(tmp);

    logger.info("--- MstCheckGroupService.saveCheckGroupRegister END ---");
    return result;

  }

  /**
   * 全てのチェックグループを取得します。
   * 
   * @return チェックグループのリスト
   */
  public List<MstCheckChangeRegistration> getAllGroupes() {

    logger.info("--- MstCheckGroupService.getAllGroupes START ---");

    List<MstCheckChangeRegistration> groupes = this.mstCheckChangeRegistrationRepositry.findAll();// リポジトリーで作成したJPAから一覧表示してエンティティに代入

    logger.info("--- MstCheckGroupService.getAllGroupes END ---");

    return groupes;// 代入した変数を返す
  }

  /**
   * チェックグループを新規登録します。
   * 
   * @param groupe チェックグループ名
   * @return 登録されたチェックグループ
   */
  @Transactional(rollbackFor = Exception.class)
  public MstCheckChangeRegistration saveCheckChange(String groupe) {// 新規登録メソッド

    logger.info("--- MstCheckGroupService.saveCheckGroup START ---");

    MstCheckChangeRegistration checkChange = new MstCheckChangeRegistration();// エンティティインスタンス作成

    checkChange.setCheckGroupName(groupe);// グループ名セット
    checkChange.setStatus(Constants.STATUS_TRUE);
    checkChange.setUpdatedMstUserId(this.commonService.getLoginUserId());
    checkChange.setCreatedAt(LocalDateTime.now());
    checkChange.setUpdatedAt(LocalDateTime.now());

    MstCheckChangeRegistration result = this.mstCheckChangeRegistrationRepositry.save(checkChange);// セットしたものをレポジトリーのJPAでSQLへインサート

    logger.info("--- MstCheckGroupService.saveCheckGroup END ---");

    return result;
  }

  /**
   * 指定されたIDのチェックグループを更新します。
   * 
   * @param name チェックグループ名
   * @param id チェックグループID
   * @return 更新されたチェックグループ
   */
  @Transactional(rollbackFor = Exception.class)
  public MstCheckChangeRegistration updateCheckChange(String name, Integer id) {// 項目変更メソッド

    logger.info("--- MstCheckGroupService.updateCheckGroup START ---");

    Optional<MstCheckChangeRegistration> optionalChange =
        this.mstCheckChangeRegistrationRepositry.findById(id);

    // 存在しないIDを指定した場合はエラーとする
    if (!optionalChange.isPresent()) {
      throw new IllegalArgumentException("Invalid group ID: " + id);
    }

    MstCheckChangeRegistration change = optionalChange.get();
    change.setCheckGroupName(name);
    change.setUpdatedMstUserId(this.commonService.getLoginUserId());
    change.setUpdatedAt(LocalDateTime.now());

    MstCheckChangeRegistration changeUpdate = this.mstCheckChangeRegistrationRepositry.save(change);

    logger.info("--- MstCheckGroupService.updateCheckGroup END ---");

    return changeUpdate;
  }

  /**
   * 指定されたIDのチェックグループのステータスを更新します。
   * 
   * @param id チェックグループID
   * @return 更新されたチェックグループ
   */
  @Transactional(rollbackFor = Exception.class)
  public MstCheckChangeRegistration updateStatusCheckChange(Integer id) {// ステータス変更メソッド

    logger.info("--- MstCheckGroupService.updateStatusCheckGroup START ---");

    Optional<MstCheckChangeRegistration> optionalChange =
        this.mstCheckChangeRegistrationRepositry.findById(id);

    // 存在しないIDを指定した場合はエラーとする
    if (!optionalChange.isPresent()) {
      throw new IllegalArgumentException("Invalid group ID: " + id);
    }

    MstCheckChangeRegistration change = optionalChange.get();
    change.setUpdatedAt(LocalDateTime.now());
    change.setUpdatedMstUserId(this.commonService.getLoginUserId());


    MstCheckChangeRegistration changeUpdate = this.mstCheckChangeRegistrationRepositry.save(change);

    logger.info("--- MstCheckGroupService.updateStatusCheckGroup END ---");

    return changeUpdate;
  }

  // ステータス切り替えするメソッド
  public void toggleStatus(int id) {
    this.mstCheckChangeRegistrationRepositry.toggleStatus(id);
  }

  /**
   * 指定されたIDのチェックグループを変更テーブルに挿入します。
   * 
   * @param id チェックグループID
   * @return 挿入されたチェックグループ変更
   */
  @Transactional(rollbackFor = Exception.class)
  public MstCheckChange updateCheckChange(Integer id) { // 変更テーブルへの挿入

    MstCheckChangeRegistration tmp = this.mstCheckChangeRegistrationRepositry.findById(id)
        .orElse(new MstCheckChangeRegistration());

    MstCheckChange mstcheckchange = new MstCheckChange();

    mstcheckchange.setUpdateId(id);
    mstcheckchange.setUpdatedAt(LocalDateTime.now());
    mstcheckchange.setStatus(tmp.getStatus());

    MstCheckChange result = this.mstCheckChangeRepository.save(mstcheckchange);// セットしたものをレポジトリーのJPAでSQLへインサート

    return result;
  }

  /**
   * Formに格納された情報をEntityに変換する
   * 
   * @param checkChangeForm
   * @return MstCheckChange
   */
  public MstCheckChangeRegistration toMstCheckChangeRegistration(
      MstCheckChangeForm checkChangeForm) {

    MstCheckChangeRegistration tmp = new MstCheckChangeRegistration();
    tmp.setId(checkChangeForm.getId());
    tmp.setCheckGroupName(checkChangeForm.getCheckGroupName());

    try {
      tmp.setStatus(Integer.parseInt(checkChangeForm.getStatus()));
    } catch (NumberFormatException e) {
      logger.error("Error status MstCheckChange : " + tmp.toString(), e);
      throw new RuntimeException("ステータスが正常ではありません", e);
    }

    tmp.setUpdatedAt(checkChangeForm.getUpdatedAt());
    tmp.setCreatedAt(checkChangeForm.getCreatedAt());
    tmp.setUpdatedMstUserId(checkChangeForm.getUpdatedMstUserId());

    return tmp;
  }



  /**
   * チェックグループを検索します。
   * 
   * @param mstCheckChangeForm 検索条件を含むフォーム
   * @return 検索結果のチェックグループフォームのリスト
   */
  @Override
  public List<MstCheckChangeRegistration> search(MstCheckChangeForm mstCheckChangeForm) {
    String checkGroupName = "";
    String status = "";
    String createdAt = "";
    String updatedAt = "";

    MstCheckChangeForm checkGroup = mstCheckChangeForm;

    checkGroupName += nullCheck(checkGroup.getCheckGroupName());
    if (checkGroup.getStatus() == null || checkGroup.getStatus().equals("")) {
      status += "";
    } else {
      if (checkGroup.getStatus().equals("active")) {
        status += "1";
      } else {
        status += "0";
      }
    }
    createdAt += nullCheck(checkGroup.getCreatedAt1());
    updatedAt += nullCheck(checkGroup.getUpdatedAt1());

    List<MstCheckChangeRegistration> a =
        mstCheckChangeRegistrationRepositry.search(checkGroupName, status, createdAt, updatedAt);

    List<MstCheckChangeRegistration> mstCheckGroupFormList = new ArrayList<>();
    for (MstCheckChangeRegistration checkGroup1 : a) {
      mstCheckGroupFormList.add(checkGroup1);
    }
    return mstCheckGroupFormList;
  }


  /**
   * 指定されたIDのチェックグループを取得します。
   * 
   * @param id チェックグループID
   * @return チェックグループ
   */
  public MstCheckChangeRegistration findById(Integer id) {
    return this.mstCheckChangeRegistrationRepositry.findById(id)
        .orElse(new MstCheckChangeRegistration());
  }

  public void download(List<MstCheckChangeRegistration> checkChange) {
    try {
      excelFileService.exportDataTypeExcel(checkChange, "Auth");
    } catch (IOException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }

  public List<MstCheckChangeRegistration> changeForm(List<MstCheckChangeForm> checkChangeForm) {
    List<MstCheckChangeRegistration> checkChange = new ArrayList<>();
    for (MstCheckChangeForm form : checkChangeForm) {
      checkChange.add(toMstCheckChangeRegistration(form));
    }
    return checkChange;
  }

  public void upload() {
    excelFileService.setSheetName("users");
    int a = excelFileService.getRowNum();
    for (int i = 1; i <= a; i++) {
      mstCheckChangeRegistrationRepositry.save(
          excelFileService.importDataToRowTypeExcel(new MstCheckChangeRegistration(), "auth", i));
    }
  }

  public List<MstCheckChangeRegistration> groupNameByCustomerId(int customerId) {
    return this.mstCheckChangeRegistrationRepositry.getGroupNameByCustomerId(customerId);
  }

}
