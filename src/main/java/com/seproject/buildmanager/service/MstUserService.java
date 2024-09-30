package com.seproject.buildmanager.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.common.ExcelFileService;
import com.seproject.buildmanager.entity.MstUser;
import com.seproject.buildmanager.form.MstUserForm;
import com.seproject.buildmanager.form.ResetPasswordForm;
import com.seproject.buildmanager.repository.MstUserRepository;

/**
 * ユーザに関するビジネスロジックを提供するサービスクラスです。
 * 
 * <p>
 * このクラスは、ユーザーのCRUD操作およびその他のビジネスロジックを処理します。
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
public class MstUserService implements MstSearchService<MstUserForm, MstUser> {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  @Autowired
  private MstUserRepository mstUserRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private CommonService commonService;

  private final ExcelFileService<MstUser> excelFileService;

  /**
   * コンストラクタ。
   * 
   * @param mstUserRepository ユーザリポジトリ
   * @param passwordEncoder パスワードエンコーダー
   * @param commonService 共通サービス
   */
  public MstUserService(MstUserRepository mstUserRepository, PasswordEncoder passwordEncoder,
      CommonService commonService, ExcelFileService<MstUser> excelFileService) {
    super();
    this.mstUserRepository = mstUserRepository;
    this.passwordEncoder = passwordEncoder;
    this.commonService = commonService;
    this.excelFileService = excelFileService;
    this.excelFileService.initializeExcel("ユーザー管理.xlsx");
  }

  /**
   * 全てのユーザを取得します。
   * 
   * @return ユーザのリスト
   */
  public List<MstUser> getAllUsers() {
    logger.info("--- MstUserService.getAllUsers START ---");

    List<MstUser> users = this.mstUserRepository.findAll();

    logger.info("--- MstUserService.getAllUsers END ---");

    return users;
  }

  /**
   * 全てのユーザを権限情報と共に取得します。
   * 
   * @return 権限情報を含む全ユーザのリスト
   */
  public List<MstUser> findAllUsersWithAuthName() {
    logger.info("--- MstUserService.findAllUsersWithAuthName START ---");

    List<MstUser> users = this.mstUserRepository.findAllUsersWithAuthName();

    logger.info("--- MstUserService.findAllUsersWithAuthName END ---");

    return users;
  }

  /**
   * 新規登録用のフォームを作成し返却します。
   * 
   * @return MstUserForm ユーザフォーム
   */
  public MstUserForm showUserForm() {
    logger.info("--- MstUserService.showUserForm START ---");

    MstUserForm tmp = new MstUserForm();

    logger.info("--- MstUserService.showUserForm END ---");

    return tmp;
  }

  /**
   * ユーザの新規登録または変更を行い、DBに保存します。
   * 
   * @param mstUserForm ユーザフォーム
   * @return MstUser 登録結果
   */
  public MstUser saveUser(MstUserForm mstUserForm) {
    logger.info("--- MstUserService.saveUser START ---");

    MstUser tmp = new MstUser();

    // 変更の場合はIDを持つため、変更内容に含める。新規の場合はインクリメントされた値が採番される
    if (mstUserForm.getId() != null) {
      tmp = this.getUserById(mstUserForm.getId());
    } else {
      // 以下は新規登録時のみ反映、変更時はそのまま使用する
      tmp.setLoginCd(mstUserForm.getLoginCd());
      tmp.setPassword(this.passwordEncoder.encode(mstUserForm.getPassword()));
      tmp.setEmail(mstUserForm.getEmail());
      tmp.setStatus(Constants.STATUS_TRUE);
    }

    tmp.setMstAuthId(mstUserForm.getMstAuthId());
    tmp.setLName(mstUserForm.getLName());
    tmp.setFName(mstUserForm.getFName());
    tmp.setLNameKana(mstUserForm.getLNameKana());
    tmp.setFNameKana(mstUserForm.getFNameKana());
    tmp.setTel(mstUserForm.getTel());

    // 更新ユーザIDはログインしているユーザのIDを指定
    tmp.setUpdatedMstUserId(this.commonService.getLoginUserId());

    MstUser result;
    try {
      result = this.mstUserRepository.save(tmp);
    } catch (Exception e) {
      logger.error("Error saving MstUser : " + tmp.toString(), e);
      throw new RuntimeException("ユーザの登録・更新に失敗しました。", e);
    }

    logger.info("--- MstUserService.saveUser END ---");
    return result;
  }

  /**
   * 特定のユーザIDのユーザを取得します。
   * 
   * @param id ユーザID
   * @return MstUser ユーザ情報
   */
  public MstUser getUserById(Integer id) {
    return this.mstUserRepository.findById(id).orElse(new MstUser());
  }

  // 追記
  public ResetPasswordForm resetPasswordForm() {
    ResetPasswordForm tmp = new ResetPasswordForm();
    return tmp;
  }

  public MstUser update(ResetPasswordForm resetPasswordForm) {
    logger.info("--- MstUserService.saveUser START ---");

    MstUser tmp = mstUserRepository.findByLogin(resetPasswordForm.getLoginCd());

    tmp.setPassword(this.passwordEncoder.encode(resetPasswordForm.getPassword()));
    // 更新ユーザIDはログインしているユーザのIDを指定
    tmp.setUpdatedMstUserId(this.commonService.getLoginUserId());

    MstUser result = this.mstUserRepository.save(tmp);

    logger.info("--- MstUserService.saveUser END ---");
    return result;
  }

  public MstUser formConvertEntity(MstUserForm userForm) {
    MstUser tmp = new MstUser();
    if (userForm.getCreatedAt() != null && !userForm.getCreatedAt().equals("")) {
      String localtime = userForm.getCreatedAt();
      localtime = localtime.replace("T", " ");
      LocalDateTime localDateTime =
          LocalDateTime.parse(localtime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      tmp.setCreatedAt(localDateTime);
    }
    if (userForm.getUpdatedAt() != null && !userForm.getUpdatedAt().equals("")) {
      String localtime = String.valueOf(userForm.getUpdatedAt());
      localtime = localtime.replace("T", " ");
      LocalDateTime localDateTime =
          LocalDateTime.parse(localtime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      tmp.setUpdatedAt(localDateTime);
    }
    tmp.setEmail(userForm.getEmail());
    tmp.setFName(userForm.getFName());
    tmp.setLName(userForm.getLName());
    tmp.setFNameKana(userForm.getFNameKana());
    tmp.setLNameKana(userForm.getLNameKana());
    tmp.setLoginCd(userForm.getLoginCd());
    tmp.setRole(userForm.getRoles());
    tmp.setTel(userForm.getTel());
    if (userForm.getStatus() != null && !userForm.getStatus().equals("")) {
      tmp.setStatus(Integer.parseInt(userForm.getStatus()));
    }

    tmp.setMstAuthId(userForm.getMstAuthId());
    tmp.setPassword(userForm.getPassword());
    tmp.setId(userForm.getId());
    return tmp;
  }

  @Override
  public List<MstUser> search(MstUserForm mstUserForm) {
    String loginCd = "";
    String userName = "";
    String userNameKana = "";
    String tel = "";
    String role = "";
    String email = "";
    String status = "";
    String createdAt = "";
    String updatedAt = "";


    MstUserForm user = mstUserForm;

    loginCd += this.nullCheck(user.getLoginCd());
    userName += this.nullCheck(user.getLName());
    userNameKana += this.nullCheck(user.getLNameKana());
    email += this.nullCheck(user.getEmail());
    role += this.nullCheck(user.getRoles());
    tel += this.nullCheck(user.getTel());
    if (user.getStatus() == null || user.getStatus().equals("")) {
      status += "";
    } else {
      if (user.getStatus().equals("1")) {
        status += "1";
      } else {
        status += "0";
      }
    }
    createdAt += this.nullCheck(user.getCreatedAt());
    updatedAt += this.nullCheck(user.getUpdatedAt());



    List<MstUser> a = mstUserRepository.search(loginCd, role, userName, userNameKana, tel, email,
        status, createdAt, updatedAt);

    return a;
  }

  public void download(List<MstUser> users) {
    try {
      this.excelFileService.exportDataTypeExcel(users, "users");
    } catch (IOException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }

  public void upload() {
    this.excelFileService.setSheetName("users");
    int a = this.excelFileService.getRowNum();
    for (int i = 1; i <= a; i++) {
      mstUserRepository
          .save(this.excelFileService.importDataToRowTypeExcel(new MstUser(), "users", i));
    }

  }

}
