package com.seproject.buildmanager.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.ExcelFileService;
import com.seproject.buildmanager.common.MstCodeEnums;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.form.MstCustomerForm;
import com.seproject.buildmanager.repository.MstCustomerRepository;


@Service
public class MstCustomerService implements MstSearchService<MstCustomerForm, MstCustomerForm> {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstCustomerRepository mstCustomerRepository;

  private final MstCodeService mstCodeService;

  private final ExcelFileService<MstCustomer> excelFileService;

  // Enumから都道府県のcode_kindの値を取得
  private static final int PREFECTURES = MstCodeEnums.PREFECTURES.getValue();

  // 同じく法人・個人のcode_kindの値を取得
  private static final int INDIVIDUAL_CORPORATE = MstCodeEnums.INDIVIDUAL_CORPORATE.getValue();

  private MstCustomerService(MstCustomerRepository mstCustomerRepository,
      MstCodeService mstCodeService, ExcelFileService<MstCustomer> excelFileService,
      CommonService commonService) {
    super();
    this.mstCustomerRepository = mstCustomerRepository;
    this.mstCodeService = mstCodeService;
    this.excelFileService = excelFileService;
    this.excelFileService.initializeExcel("顧客管理データ.xlsx");
  }


  public List<MstCustomer> getAllCustomers() {

    logger.info("--- MstCustomerService.getAllCustomers START ---");

    List<MstCustomer> customer = mstCustomerRepository.findAll();

    logger.info("--- MstCustomerService.getAllCustomer END ---");

    return customer;

  }

  public List<MstCustomerForm> viewCustomerForm() {

    List<MstCustomer> mstCustomer = getAllCustomers();
    List<MstCustomerForm> mstCustomerForm = new ArrayList<MstCustomerForm>();
    for (MstCustomer customer : mstCustomer) {
      // MstCustomer cus = mstCustomerService.getCustomerById(owner.getClientId());
      mstCustomerForm.add(updateCustomerForm(customer/* cus.getCorpName() */));
    }
    return mstCustomerForm;
  }

  public MstCustomerForm showCustomerForm() {

    logger.info("--- MstCustomerService.showCustomerForm START ---");

    MstCustomerForm tmp = new MstCustomerForm();

    logger.info("--- MstCustomerService.showCustomerForm END ---");

    return tmp;

  }

  public MstCustomer saveCustomer(MstCustomerForm mstCustomerForm) {// 登録

    logger.info("--- MstCustomerService.saveCustomer START ---");

    MstCustomer tmp = new MstCustomer();

    if (mstCustomerForm.getId() != null) {
      tmp.setId(mstCustomerForm.getId());
      tmp.setCreatedAt(mstCustomerForm.getCreatedAt());
      tmp.setUpdatedAt(LocalDateTime.now());
    } else {
      tmp.setCreatedAt(LocalDateTime.now());
      tmp.setUpdatedAt(LocalDateTime.now());
    }

    tmp.setCorpName(mstCustomerForm.getCorpName());
    tmp.setCorpKana(mstCustomerForm.getCorpKana());
    tmp.setDepartment(mstCustomerForm.getDepartment());
    tmp.setLName(mstCustomerForm.getLName());
    tmp.setFName(mstCustomerForm.getFName());
    tmp.setLNameKana(mstCustomerForm.getLNameKana());
    tmp.setFNameKana(mstCustomerForm.getFNameKana());
    tmp.setZip(mstCustomerForm.getZip());
    tmp.setAddress1(mstCustomerForm.getAddress1());
    tmp.setAddress2(mstCustomerForm.getAddress2());
    tmp.setTel(mstCustomerForm.getTel());
    tmp.setMobile(mstCustomerForm.getMobile());
    tmp.setMail(mstCustomerForm.getMail());

    if (mstCustomerForm.isMailFlg()) {
      tmp.setMailFlg(0);
    } else {
      tmp.setMailFlg(1);
    }


    MstCode mstCode =
        mstCodeService.getCodeByKindAndName(mstCustomerForm.getCust_kind(), INDIVIDUAL_CORPORATE);
    tmp.setCustKind(mstCode.getCodeBranchNum());

    mstCode = mstCodeService.getCodeByKindAndName(mstCustomerForm.getPrefectures(), PREFECTURES);
    tmp.setPrefecture(mstCode.getCodeBranchNum());



    try {
      tmp.setStatus(Integer.valueOf(mstCustomerForm.getStatus()));
    } catch (NumberFormatException e) {
      tmp.setStatus(1);
    }
    // TODO:登録ユーザは未対応
    MstCustomer result = mstCustomerRepository.save(tmp);

    logger.info("--- MstCustomerService.saveCustomer END ---");
    return result;

  }

  public MstCustomer getCustomerById(Integer id) {
    return mstCustomerRepository.findById(id).orElse(new MstCustomer());
  }

  public MstCustomer saveCustomerUpdate(MstCustomerForm mstCustomerForm) {// 変更

    logger.info("--- MstCustomerService.saveCustomer START ---");

    MstCustomer tmp = new MstCustomer();

    tmp.setCorpName(mstCustomerForm.getCorpName());
    tmp.setCorpKana(mstCustomerForm.getCorpKana());
    tmp.setDepartment(mstCustomerForm.getDepartment());
    tmp.setLName(mstCustomerForm.getLName());
    tmp.setLNameKana(mstCustomerForm.getLNameKana());
    tmp.setFName(mstCustomerForm.getFName());
    tmp.setFNameKana(mstCustomerForm.getFNameKana());
    tmp.setZip(mstCustomerForm.getZip());
    tmp.setAddress1(mstCustomerForm.getAddress1());
    tmp.setAddress2(mstCustomerForm.getAddress2());
    tmp.setTel(mstCustomerForm.getTel());
    tmp.setMobile(mstCustomerForm.getMobile());
    tmp.setMail(mstCustomerForm.getMail());

    if (mstCustomerForm.isMailFlg()) {
      tmp.setMailFlg(0);
    } else {
      tmp.setMailFlg(1);
    }


    MstCode mstCode =
        mstCodeService.getCodeByKindAndName(mstCustomerForm.getCust_kind(), INDIVIDUAL_CORPORATE);
    tmp.setCustKind(mstCode.getCodeBranchNum());

    mstCode = mstCodeService.getCodeByKindAndName(mstCustomerForm.getPrefectures(), PREFECTURES);
    tmp.setPrefecture(mstCode.getCodeBranchNum());

    tmp.setUpdatedAt(null);

    try {
      tmp.setStatus(Integer.valueOf(mstCustomerForm.getStatus()));
    } catch (NumberFormatException e) {
      tmp.setStatus(1);
    }
    tmp.setUpdatedAt(LocalDateTime.now());
    MstCustomer result = mstCustomerRepository.save(tmp);

    logger.info("--- MstCustomerService.saveUser END ---");
    return result;

  }

  // 表示用にエンティティからとってきたデータをフォームに格納する
  public MstCustomerForm updateCustomerForm(MstCustomer mstCustomer) {
    MstCustomerForm tmp = new MstCustomerForm();

    tmp.setId(mstCustomer.getId());
    tmp.setCreatedAt(mstCustomer.getCreatedAt());
    tmp.setUpdatedAt(mstCustomer.getUpdatedAt());
    tmp.setStatus(String.valueOf(mstCustomer.getStatus()));
    tmp.setCorpName(mstCustomer.getCorpName());
    tmp.setCorpKana(mstCustomer.getCorpKana());
    tmp.setDepartment(mstCustomer.getDepartment());
    tmp.setLName(mstCustomer.getLName());
    tmp.setLNameKana(mstCustomer.getLNameKana());
    tmp.setFName(mstCustomer.getFName());
    tmp.setFNameKana(mstCustomer.getFNameKana());
    tmp.setZip(mstCustomer.getZip());
    tmp.setAddress1(mstCustomer.getAddress1());
    tmp.setAddress2(mstCustomer.getAddress2());
    tmp.setTel(mstCustomer.getTel());
    tmp.setMobile(mstCustomer.getMobile());
    tmp.setMail(mstCustomer.getMail());


    // mstCodeRepesitory.findByCodeKindAndBranchNum code_kindとcode_branch_numからデータを検索する
    // 第一引数、code_kind 第二引数、code_branch_num(エンティティに格納されている値)
    MstCode mstCode =
        mstCodeService.getCodeByKindAndBranch(INDIVIDUAL_CORPORATE, mstCustomer.getCustKind());
    tmp.setCust_kind(mstCode.getCodeName()); // 取得した値から名前を取得
    mstCode = mstCodeService.getCodeByKindAndBranch(PREFECTURES, mstCustomer.getPrefecture());
    tmp.setPrefectures(mstCode.getCodeName());
    return tmp;
  }

  public MstCustomer saveStatus(Integer id) {
    MstCustomer customer = getCustomerById(id);
    customer.setUpdatedAt(LocalDateTime.now());

    MstCustomer result = mstCustomerRepository.save(customer);
    mstCustomerRepository.toggleStatus(id);
    return result;
  }

  // @Override
  public List<MstCustomerForm> search(MstCustomerForm mstCustomerForm) {
    String cust_kind = "";
    String corpName = "";
    String corpKana = "";
    String department = "";
    String customerName = "";
    String customerNameKana = "";
    String zip = "";
    String prefecture = "";
    String address1 = "";
    String address2 = "";
    String tel = "";
    String mobile = "";
    String mail = "";
    String status = "";
    String createdAt = "";
    String updatedAt = "";

    MstCustomerForm customer = mstCustomerForm;

    if (customer.getCust_kind() == null || customer.getCust_kind().equals("")) {
      cust_kind += "";
    } else {
      MstCode mstCode =
          mstCodeService.getCodeByKindAndName(customer.getCust_kind(), INDIVIDUAL_CORPORATE);
      customer.setCust_kind(mstCode.getCodeBranchNum().toString());
      cust_kind += customer.getCust_kind();
    }
    corpName += this.nullCheck(customer.getCorpName());
    corpKana += this.nullCheck(customer.getCorpKana());
    department += this.nullCheck(customer.getDepartment());
    customerName += this.nullCheck((customer.getLName()));
    customerNameKana += this.nullCheck((customer.getLNameKana()));
    zip += this.nullCheck(customer.getZip());

    if (customer.getPrefectures() == null || customer.getPrefectures().equals("")) {
      prefecture += "";
    } else {
      MstCode mstCode = mstCodeService.getCodeByKindAndName(customer.getPrefectures(), PREFECTURES);
      customer.setPrefectures(mstCode.getCodeBranchNum().toString());
      prefecture += customer.getPrefectures();
    }
    address1 += this.nullCheck(customer.getAddress1());
    address2 += this.nullCheck(customer.getAddress2());
    tel += this.nullCheck(customer.getTel());
    mobile += this.nullCheck(customer.getMobile());
    if (customer.getStatus() == null || customer.getStatus().equals("")) {
      status += "";
    } else {
      if (customer.getStatus().equals("active")) {
        status += "1";
      } else {
        status += "0";
      }
    }
    createdAt += this.nullCheck(customer.getCreatedAt1());
    updatedAt += this.nullCheck(customer.getUpdatedAt1());



    List<MstCustomer> a = mstCustomerRepository.search(cust_kind, corpName, corpKana, department,
        customerName, customerNameKana, zip, prefecture, address1, address2, tel, mobile, mail,
        status, createdAt, updatedAt);
    List<MstCustomerForm> mstCustomerFormList = new ArrayList<>();
    for (MstCustomer customer1 : a) {
      mstCustomerFormList.add(updateCustomerForm(customer1));
    }
    return mstCustomerFormList;
  }

  public void download(List<MstCustomer> customer) {
    try {
      this.excelFileService.exportDataTypeExcel(customer, "customer");
    } catch (IOException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }

  public List<MstCustomer> changeCustomerForm(List<MstCustomerForm> customerForm) {
    List<MstCustomer> customer = new ArrayList<>();
    for (MstCustomerForm of : customerForm) {
      customer.add(saveCustomerUpdate(of));
    }
    return customer;
  }

  public void upload() {
    this.excelFileService.setSheetName("customer");
    int num = this.excelFileService.getRowNum();
    for (int i = 1; i <= num; i++) {
      mstCustomerRepository
          .save(this.excelFileService.importDataToRowTypeExcel(new MstCustomer(), "customer", i));
    }
  }
}


