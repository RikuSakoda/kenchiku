package com.seproject.buildmanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.common.ExcelFileService;
import com.seproject.buildmanager.common.MstCodeEnums;
import com.seproject.buildmanager.common.PDFFileService;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.entity.MstOwnerManagement;
import com.seproject.buildmanager.form.MstOwnerManagementForm;
import com.seproject.buildmanager.repository.MstOwnerManagementRepository;

@Service
public class MstOwnerManagementService
    implements MstSearchService<MstOwnerManagementForm, MstOwnerManagementForm> {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstOwnerManagementRepository mstOwnerRepository;

  private final MstCodeService mstCodeService;

  private final MstCustomerService mstCustomerService;

  private final ExcelFileService<MstOwnerManagement> excelFileService;

  private final PDFFileService pdfFileService;

  private final CommonService commonService;

  // Enumから都道府県のcode_kindの値を取得
  private static final int PREFECTURES = MstCodeEnums.PREFECTURES.getValue();

  // 同じく法人・個人のcode_kindの値を取得
  private static final int INDIVIDUAL_CORPORATE = MstCodeEnums.INDIVIDUAL_CORPORATE.getValue();


  // private ExcelFileService<MstOwner> fileService = new
  // ExcelFileService<MstOwner>("オーナーデータ.xlsx");

  public MstOwnerManagementService(MstOwnerManagementRepository mstOwnerRepository,
      MstCodeService mstCodeService, MstCustomerService mstCustomerService,
      ExcelFileService<MstOwnerManagement> excelFileService, CommonService commonService,
      PDFFileService pdfFileService) {
    super();
    this.mstOwnerRepository = mstOwnerRepository;
    this.mstCodeService = mstCodeService;
    this.mstCustomerService = mstCustomerService;
    this.excelFileService = excelFileService;
    this.excelFileService.initializeExcel("オーナー管理.xlsx");
    this.commonService = commonService;
    this.pdfFileService = pdfFileService;
    this.pdfFileService.initializePDF("test.pdf");
  }

  /**
   * 全てのオーナーを取得します。
   * 
   * @return オーナーのリスト
   */
  public List<MstOwnerManagement> getAllOwner() {

    logger.info("--- MstOwnerService.getAllOwner START ---");

    List<MstOwnerManagement> owners = mstOwnerRepository.findAll();

    logger.info("--- MstOwnerService.getAllUsers END ---");

    return owners;

  }

  /**
   * 全てのオーナーを顧客名と共に取得します。
   * 
   * @return 顧客名を含む全オーナーのリスト
   */
  public List<MstOwnerManagement> getAllOwnerWithCustmerName() {

    logger.info("--- MstOwnerService.getAllOwnerWithCustmerName START ---");


    List<MstOwnerManagement> owners = mstOwnerRepository.findAllWithClientName();

    logger.info("--- MstOwnerService.getAllOwnerWithCustmerName END ---");

    return owners;

  }

  /**
   * 新規登録用のフォームを作成し返却します。
   * 
   * @return MstOwnerForm ユーザフォーム
   */
  public MstOwnerManagementForm showOwnerForm() {

    logger.info("--- MstOwnerService.showOwnerForm START ---");

    MstOwnerManagementForm tmp = new MstOwnerManagementForm();

    logger.info("--- MstOwnerService.showOwnerForm END ---");

    return tmp;

  }

  /**
   * 全てのオーナーを顧客名と共に取得します。
   * 
   * @return 顧客名を含む全オーナーのリスト
   */
  public List<MstOwnerManagementForm> viewOwnerForm() {

    List<MstOwnerManagement> mstOwner = getAllOwnerWithCustmerName();
    List<MstOwnerManagementForm> mstOwnerFormList = new ArrayList<>();
    for (MstOwnerManagement owner : mstOwner) {
      // MstCustomer cus = mstCustomerService.getCustomerById(owner.getClientId());
      mstOwnerFormList.add(findByIdConvertOwnerForm(owner.getId()));
    }
    return mstOwnerFormList;
  }

  /**
   * 表示用にエンティティからとってきたデータをフォームに格納する
   * 
   * @param MstOwnerManagement オーナー
   * @param cusName 顧客名
   * @return MstOwnerForm オーナーフォーム
   */
  // 表示用にエンティティからとってきたデータをフォームに格納する
  // 引数からデータをとってくるように変更
  public MstOwnerManagementForm findByIdConvertOwnerForm(Integer id) {
    MstOwnerManagement mstOwner =
        mstOwnerRepository.findByIdWithClientName(id).orElse(new MstOwnerManagement()); // ClientNameを含めたmstOwnerを1行取得
    MstOwnerManagementForm tmp = new MstOwnerManagementForm();
    tmp.setCreatedAt(mstOwner.getCreatedAt());
    tmp.setUpdatedAt(mstOwner.getUpdatedAt());
    tmp.setAddress(mstOwner.getAddress());
    tmp.setBuildingName(mstOwner.getBuildingName());
    tmp.setClient(mstOwner.getClientName());
    tmp.setClientId(String.valueOf(mstOwner.getClientId()));
    tmp.setCorporation(mstOwner.getCorporation());
    tmp.setCorporationKana(mstOwner.getCorporationKana());
    tmp.setDepartment(mstOwner.getDepartment());
    tmp.setEmail(mstOwner.getEmail());
    tmp.setFName(mstOwner.getFName());
    tmp.setFNameKana(mstOwner.getFNameKana());
    tmp.setId(String.valueOf(mstOwner.getId()));

    // mstCodeRepesitory.findByCodeKindAndBranchNum code_kindとcode_branch_numからデータを検索する
    // 第一引数、code_kind 第二引数、code_branch_num(エンティティに格納されている値)
    MstCode mstCode =
        mstCodeService.getCodeByKindAndBranch(INDIVIDUAL_CORPORATE, mstOwner.getIndividual());
    tmp.setIndividual(mstCode.getCodeName()); // 取得した値から名前を取得
    tmp.setLName(mstOwner.getLName());
    tmp.setLNameKana(mstOwner.getLNameKana());
    tmp.setMobilePhone(mstOwner.getMobilePhone());
    tmp.setPhone(mstOwner.getPhone());
    tmp.setPostCode(mstOwner.getPostCode());
    mstCode = mstCodeService.getCodeByKindAndBranch(PREFECTURES, mstOwner.getPrefectures());
    tmp.setPrefectures(String.valueOf(mstCode.getCodeName()));
    tmp.setStatus(String.valueOf(mstOwner.getStatus()));
    return tmp;
  }

  public MstOwnerManagementForm OwnerForm(MstOwnerManagementForm mstOwnerForm) {

    MstCustomer m =
        mstCustomerService.getCustomerById(Integer.parseInt(mstOwnerForm.getClientId()));
    mstOwnerForm.setClient(m.getCorpName());
    return mstOwnerForm;
  }

  /**
   * オーナーの新規登録を行い、DBに保存します。
   * 
   * @param MstOwnerManagementForm オーナーフォーム
   * @return MstOwner 登録結果
   */
  public MstOwnerManagement saveOwnerRegister(MstOwnerManagementForm mstOwnerForm) {// 登録

    logger.info("--- MstOwnerService.saveOwner START ---");

    MstOwnerManagement tmp = toMstOwner(mstOwnerForm);

    tmp.setId(null);
    tmp.setStatus(Constants.STATUS_TRUE);
    tmp.setCreatedAt(LocalDateTime.now());
    tmp.setUpdatedAt(LocalDateTime.now());

    MstOwnerManagement result;
    try {
      result = mstOwnerRepository.save(tmp);
    } catch (Exception e) {
      logger.error("Error saving MstOwner : " + tmp.toString(), e);
      throw new RuntimeException("オーナーの登録に失敗しました。", e);
    }

    logger.info("--- MstOwnerService.saveOwner END ---");
    return result;

  }

  /**
   * idでオーナー検索
   * 
   * @param id オーナーid
   * @return MstOwner 検索結果
   */
  public MstOwnerManagement findOwnerById(int id) {// id検索

    return mstOwnerRepository.findById(id).orElse(new MstOwnerManagement());

  }

  /**
   * オーナーの変更を行い、DBに保存します。
   * 
   * @param MstOwnerManagementForm オーナーフォーム
   * @return MstOwner 変更結果
   */
  public MstOwnerManagement saveOwnerUpdate(MstOwnerManagementForm mstOwnerForm) {// 変更

    logger.info("--- MstOwnerService.saveUser START ---");

    MstOwnerManagement tmp = toMstOwner(mstOwnerForm);
    tmp.setUpdatedAt(LocalDateTime.now());
    MstOwnerManagement result;
    try {
      result = mstOwnerRepository.save(tmp);
    } catch (Exception e) {
      logger.error("Error saving MstOwner : " + tmp.toString(), e);
      throw new RuntimeException("オーナーの変更に失敗しました。", e);
    }

    logger.info("--- MstOwnerService.saveUser END ---");
    return result;

  }

  /**
   * Formに格納された情報をEntityに変換する
   * 
   * @param ownerForm
   * @return MstOwner
   */
  public MstOwnerManagement toMstOwner(MstOwnerManagementForm ownerForm) {
    MstOwnerManagement tmp = new MstOwnerManagement();
    try {
      tmp.setId(Integer.parseInt(ownerForm.getId()));
    } catch (NumberFormatException | NullPointerException e) {
      tmp.setId(null);
    }
    try {
      tmp.setClientId(Integer.parseInt(ownerForm.getClientId()));
    } catch (NumberFormatException | NullPointerException e) {
      tmp.setClientId(null);
    }

    tmp.setAddress(ownerForm.getAddress());
    tmp.setBuildingName(ownerForm.getBuildingName());
    tmp.setCorporation(ownerForm.getCorporation());
    tmp.setCorporationKana(ownerForm.getCorporationKana());
    tmp.setDepartment(ownerForm.getDepartment());
    tmp.setEmail(ownerForm.getEmail());
    tmp.setFName(ownerForm.getFName());
    tmp.setFNameKana(ownerForm.getFNameKana());

    tmp.setLName(ownerForm.getLName());
    tmp.setLNameKana(ownerForm.getLNameKana());
    tmp.setMobilePhone(ownerForm.getMobilePhone());
    tmp.setPhone(ownerForm.getPhone());
    tmp.setPostCode(ownerForm.getPostCode());


    // ここ


    try {
      MstCode mstCode =
          mstCodeService.getCodeByKindAndName(ownerForm.getIndividual(), INDIVIDUAL_CORPORATE);
      tmp.setIndividual(mstCode.getCodeBranchNum());
    } catch (NumberFormatException | NullPointerException e) {
      tmp.setIndividual(null);
    }
    // ここまで

    // 同じ処理

    try {
      MstCode mstCode =
          mstCodeService.getCodeByKindAndName(ownerForm.getPrefectures(), PREFECTURES);
      tmp.setPrefectures(mstCode.getCodeBranchNum());
    } catch (NumberFormatException | NullPointerException e) {
      tmp.setPrefectures(null);
    }


    try {
      tmp.setStatus(Integer.valueOf(ownerForm.getStatus()));
    } catch (NumberFormatException | NullPointerException e) {
      tmp.setStatus(1);
    }
    tmp.setUpdatedAt(ownerForm.getUpdatedAt());
    tmp.setCreatedAt(ownerForm.getCreatedAt());
    tmp.setUpdatedMstUserId(this.commonService.getLoginUserId());// userのidがわからないため保留

    return tmp;
  }

  public MstOwnerManagement saveStatus(Integer id) {
    MstOwnerManagement owner = findOwnerById(id);
    owner.setUpdatedAt(LocalDateTime.now());

    MstOwnerManagement result = mstOwnerRepository.save(owner);
    mstOwnerRepository.toggleStatus(id);
    return result;
  }



  @Override
  public List<MstOwnerManagementForm> search(MstOwnerManagementForm mstOwnerForm) {
    String individual = "";
    String client = "";
    String corporation = "";
    String corporationKana = "";
    String department = "";
    String ownerName = "";
    String ownerNameKana = "";
    String postCode = "";
    String prefectures = "";
    String address = "";
    String buildingName = "";
    String phone = "";
    String mobilePhone = "";
    String email = "";
    String status = "";
    String createdAt = "";
    String updatedAt = "";

    MstOwnerManagementForm owner = mstOwnerForm;

    if (owner.getIndividual() == null || owner.getIndividual().equals("")) {
      individual += "";
    } else {
      MstCode mstCode =
          mstCodeService.getCodeByKindAndName(owner.getIndividual(), INDIVIDUAL_CORPORATE);
      owner.setIndividual(mstCode.getCodeBranchNum().toString());
      individual += owner.getIndividual();
    }
    client += nullCheck(owner.getClient());
    corporation += nullCheck(owner.getCorporation());
    corporationKana += nullCheck(owner.getCorporationKana());
    department += nullCheck(owner.getDepartment());
    ownerName += nullCheck(owner.getLName());
    ownerNameKana += nullCheck(owner.getLNameKana());
    postCode += nullCheck(owner.getPostCode());
    if (owner.getPrefectures() == null || owner.getPrefectures().equals("")) {
      prefectures += "";
    } else {
      MstCode mstCode = mstCodeService.getCodeByKindAndName(owner.getPrefectures(), PREFECTURES);
      owner.setPrefectures(mstCode.getCodeBranchNum().toString());
      prefectures += owner.getPrefectures();
    }
    address += nullCheck(owner.getAddress());
    buildingName += nullCheck(owner.getBuildingName());
    phone += nullCheck(owner.getPhone());
    mobilePhone += nullCheck(owner.getMobilePhone());
    email += nullCheck(owner.getEmail());
    if (owner.getStatus() == null || owner.getStatus().equals("")) {
      status += "";
    } else {
      if (owner.getStatus().equals("active")) {
        status += "1";
      } else {
        status += "0";
      }
    }
    createdAt += nullCheck(owner.getCreatedAt1());
    updatedAt += nullCheck(owner.getUpdatedAt1());



    List<MstOwnerManagement> a = mstOwnerRepository.search(individual, client, corporation,
        corporationKana, department, ownerName, ownerNameKana, postCode, prefectures, address,
        buildingName, phone, mobilePhone, email, status, createdAt, updatedAt);
    // 表示がフォームの人のみ下を記入
    List<MstOwnerManagementForm> mstOwnerFormList = new ArrayList<>();
    for (MstOwnerManagement owner1 : a) {
      mstOwnerFormList.add(findByIdConvertOwnerForm(owner1.getId()));
    }
    // ここまで
    return mstOwnerFormList;
  }

  public void download(List<MstOwnerManagement> owner) {
    this.pdfFileService.writerPDF();
    // try {
    //
    // this.excelFileService.exportDataTypeExcel(owner, "owner");
    // } catch (IOException e) {
    // // TODO 自動生成された catch ブロック
    // e.printStackTrace();
    // }
  }

  public List<MstOwnerManagement> changeOwnerForm(List<MstOwnerManagementForm> ownerForm) {
    List<MstOwnerManagement> owner = new ArrayList<>();
    for (MstOwnerManagementForm of : ownerForm) {
      owner.add(toMstOwner(of));
    }
    return owner;
  }

  public void upload() {
    try {
      this.excelFileService.setSheetName("owner");
      int num = this.excelFileService.getRowNum();
      for (int i = 1; i <= num; i++) {
        mstOwnerRepository.save(
            this.excelFileService.importDataToRowTypeExcel(new MstOwnerManagement(), "owner", i));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
