package com.seproject.buildmanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.MstCodeEnums;
import com.seproject.buildmanager.entity.MstCheckChangeRegistration;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.entity.MstEstate;
import com.seproject.buildmanager.entity.MstFloorManagement;
import com.seproject.buildmanager.entity.MstFloorName;
import com.seproject.buildmanager.entity.MstOwnerManagement;
import com.seproject.buildmanager.form.MstEstateForm;
import com.seproject.buildmanager.form.MstFloorManagementForm;
import com.seproject.buildmanager.repository.MstEstateRepository;
import com.seproject.buildmanager.repository.MstFloorManagementRepository;

@Service
public class MstFloorManagementService
    implements MstSearchService<MstFloorManagementForm, MstFloorManagementForm> {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstFloorManagementRepository mstFloorManagementRepository;

  private final MstEstateRepository mstEstateRepository;

  private final CommonService commonService;

  private final MstOwnerManagementService mstOwnerService;

  private final MstCodeService mstCodeService;

  private final FloorNameService FloorNameService;

  private final MstCheckChangeService mstCheckChangeService;

  private static final int PREFECTURES = MstCodeEnums.PREFECTURES.getValue();

  public MstFloorManagementService(MstFloorManagementRepository mstFloorManagementRepository,
      CommonService commonService, MstOwnerManagementService mstOwnerService,
      MstCodeService mstCodeService, FloorNameService FloorNameService,
      MstCheckChangeService mstCheckChangeService, MstEstateRepository mstEstateRepository) {
    this.mstFloorManagementRepository = mstFloorManagementRepository;
    this.commonService = commonService;
    this.mstOwnerService = mstOwnerService;
    this.mstCodeService = mstCodeService;
    this.FloorNameService = FloorNameService;
    this.mstCheckChangeService = mstCheckChangeService;
    this.mstEstateRepository = mstEstateRepository;
  }

  public List<MstFloorManagement> getName() {// 顧客名,オーナー名取得
    List<MstFloorManagement> floor = mstFloorManagementRepository.findDisplay();
    return floor;
  }


  public MstFloorManagementForm registerFloorForm() { // インスタンス生成
    MstFloorManagementForm tmp = new MstFloorManagementForm();
    return tmp;
  }

  public MstFloorManagement getFloorId(int id) {// id検索
    return mstFloorManagementRepository.findById(id).orElse(new MstFloorManagement());
  }

  public List<MstFloorManagementForm> viewFloorForm() { // 全件取得

    List<MstFloorManagement> mstFloor = getName();
    List<MstFloorManagementForm> mstFloorForm = new ArrayList<MstFloorManagementForm>();
    for (MstFloorManagement floor : mstFloor) {
      // MstCustomer cus = mstCustomerService.getCustomerById(owner.getClientId());
      mstFloorForm.add(updateFloorForm(floor, floor.getCustomerName(),
          floor.getOwnerName()/* cus.getCorpName() */));
    }
    return mstFloorForm;
  }

  // 表示用にエンティティからとってきたデータをフォームに格納する
  public MstFloorManagementForm updateFloorForm(MstFloorManagement mstFloor, String customerName,
      String ownerName) {
    MstFloorManagementForm tmp = new MstFloorManagementForm();
    tmp.setCustomerId(String.valueOf(mstFloor.getCustomerId())); // 顧客ID
    tmp.setCustomerName(customerName); // 顧客名
    tmp.setOwnerId(String.valueOf(mstFloor.getOwnerId())); // オーナーID
    tmp.setOwnerName(ownerName); // オーナー名
    tmp.setFloorName(mstFloor.getFloorName()); // 物件名
    tmp.setArrangement(mstFloor.getArrangement()); // 間取り
    tmp.setRent(String.valueOf(mstFloor.getRent())); // 家賃
    tmp.setSecurityDeposit(String.valueOf(mstFloor.getSecurityDeposit())); // 敷金
    tmp.setPostCode(mstFloor.getPostCode()); // 郵便番号
    tmp.setAddress(mstFloor.getAddress()); // 住所
    tmp.setBuildingName(mstFloor.getBuildingName()); // 建物名
    tmp.setPhone(mstFloor.getPhone()); // 電話番号
    tmp.setMobilePhone(mstFloor.getMobilePhone()); // 携帯番号
    tmp.setArea(String.valueOf(mstFloor.getArea())); // 坪数
    tmp.setCreatedAt(mstFloor.getCreatedAt());
    tmp.setUpdatedAt(mstFloor.getUpdatedAt());
    tmp.setId(mstFloor.getId());

    MstCode mstCode = mstCodeService.getCodeByKindAndBranch(PREFECTURES, mstFloor.getPrefectures());
    tmp.setPrefectures(mstCode.getCodeName());

    tmp.setStatus(String.valueOf(mstFloor.getStatus()));
    return tmp;
  }

  public MstFloorManagementForm FloorForm(MstFloorManagementForm mstFloorManagementForm) {
    MstOwnerManagement a =
        mstOwnerService.findOwnerById(Integer.parseInt(mstFloorManagementForm.getOwnerId()));
    mstFloorManagementForm.setOwnerName(a.getLName() + a.getFName());
    return mstFloorManagementForm;
  }

  // 登録
  public MstFloorManagement saveFloorRegister(MstFloorManagementForm mstFloorManagementForm) {

    logger.info("--- MstCostService.saveCost START ---");

    MstFloorManagement tmp = new MstFloorManagement();
    tmp.setCustomerId(Integer.parseInt(mstFloorManagementForm.getCustomerId()));
    tmp.setCustomerName(mstFloorManagementForm.getCustomerName());
    tmp.setOwnerId(Integer.parseInt(mstFloorManagementForm.getOwnerId()));
    tmp.setOwnerName(mstFloorManagementForm.getOwnerName());
    tmp.setFloorName(mstFloorManagementForm.getFloorName()); // 物件名
    tmp.setArrangement(mstFloorManagementForm.getArrangement()); // 間取り
    tmp.setRent(Integer.parseInt(mstFloorManagementForm.getRent())); // 家賃
    tmp.setSecurityDeposit(Integer.parseInt(mstFloorManagementForm.getSecurityDeposit())); // 敷金
    tmp.setPostCode(mstFloorManagementForm.getPostCode()); // 郵便番号
    tmp.setAddress(mstFloorManagementForm.getAddress()); // 住所
    tmp.setBuildingName(mstFloorManagementForm.getBuildingName()); // 建物名
    tmp.setPhone(mstFloorManagementForm.getPhone()); // 電話番号
    tmp.setMobilePhone(mstFloorManagementForm.getMobilePhone()); // 携帯番号
    tmp.setArea(Float.parseFloat(mstFloorManagementForm.getArea())); // 坪数



    MstCode mstCode =
        mstCodeService.getCodeByKindAndName(mstFloorManagementForm.getPrefectures(), PREFECTURES);
    tmp.setPrefectures(mstCode.getCodeBranchNum());

    tmp.setUpdatedAt(LocalDateTime.now());

    try {
      tmp.setStatus(Integer.valueOf(mstFloorManagementForm.getStatus()));
    } catch (NumberFormatException e) {
      tmp.setStatus(1);
    }
    tmp.setCreatedAt(LocalDateTime.now());
    // tmp.setUpdatedMstUserId();
    tmp.setUpdatedMstUserId(this.commonService.getLoginUserId());

    MstFloorManagement result = mstFloorManagementRepository.save(tmp);

    logger.info("--- MstOwnerService.saveUser END ---");
    return result;
  }

  // 変更
  public MstFloorManagement saveFloorUpdate(MstFloorManagementForm mstFloorManagementForm) {

    logger.info("--- MstCustomerService.saveCustomer START ---");

    MstFloorManagement tmp = new MstFloorManagement();
    tmp.setId(mstFloorManagementForm.getId());
    tmp.setCustomerId(Integer.parseInt(mstFloorManagementForm.getCustomerId())); // 顧客ID
    tmp.setCustomerName(mstFloorManagementForm.getCustomerName());
    tmp.setOwnerId(Integer.parseInt(mstFloorManagementForm.getOwnerId())); // 工事区分分類ID
    tmp.setOwnerName(mstFloorManagementForm.getOwnerName());
    tmp.setFloorName(mstFloorManagementForm.getFloorName()); // 物件名
    tmp.setArrangement(mstFloorManagementForm.getArrangement()); // 間取り
    tmp.setRent(Integer.parseInt(mstFloorManagementForm.getRent())); // 家賃
    tmp.setSecurityDeposit(Integer.parseInt(mstFloorManagementForm.getSecurityDeposit())); // 敷金
    tmp.setPostCode(mstFloorManagementForm.getPostCode()); // 郵便番号
    tmp.setAddress(mstFloorManagementForm.getAddress()); // 住所
    tmp.setBuildingName(mstFloorManagementForm.getBuildingName()); // 建物名
    tmp.setPhone(mstFloorManagementForm.getPhone()); // 電話番号
    tmp.setMobilePhone(mstFloorManagementForm.getMobilePhone()); // 携帯番号
    tmp.setArea(Float.parseFloat(mstFloorManagementForm.getArea())); // 坪数


    MstCode mstCode =
        mstCodeService.getCodeByKindAndName(mstFloorManagementForm.getPrefectures(), PREFECTURES);
    tmp.setPrefectures(mstCode.getCodeBranchNum());

    tmp.setUpdatedAt(null);

    try {
      tmp.setStatus(Integer.valueOf(mstFloorManagementForm.getStatus()));
    } catch (NumberFormatException e) {
      tmp.setStatus(1);
    }

    tmp.setCreatedAt(mstFloorManagementForm.getCreatedAt());
    tmp.setUpdatedAt(LocalDateTime.now());
    tmp.setUpdatedMstUserId(this.commonService.getLoginUserId());
    MstFloorManagement result = mstFloorManagementRepository.save(tmp);

    logger.info("--- MstCostService.saveCost END ---");
    return result;
  }

  // 表示用にエンティティからとってきたデータをフォームに格納する
  public MstFloorManagementForm updateFloorForm(Integer id) {
    MstFloorManagement mstFloorManagement =
        mstFloorManagementRepository.findByIdAndCustomerName(id).orElse(new MstFloorManagement()); // ClientNameを含めたmstOwnerを1行取得

    MstFloorManagementForm tmp = new MstFloorManagementForm();

    tmp.setId(mstFloorManagement.getId());
    tmp.setCustomerId(String.valueOf(mstFloorManagement.getCustomerId())); // 顧客ID
    tmp.setCustomerName(mstFloorManagement.getCustomerName());
    tmp.setOwnerId(String.valueOf(mstFloorManagement.getOwnerId())); // 工事区分分類ID
    tmp.setOwnerName(mstFloorManagement.getOwnerName());
    tmp.setFloorName(mstFloorManagement.getFloorName()); // 物件名
    tmp.setArrangement(mstFloorManagement.getArrangement()); // 間取り
    tmp.setRent(String.valueOf(mstFloorManagement.getRent())); // 家賃
    tmp.setSecurityDeposit(String.valueOf(mstFloorManagement.getSecurityDeposit())); // 敷金
    tmp.setPostCode(mstFloorManagement.getPostCode()); // 郵便番号
    tmp.setAddress(mstFloorManagement.getAddress()); // 住所
    tmp.setBuildingName(mstFloorManagement.getBuildingName()); // 建物名
    tmp.setPhone(mstFloorManagement.getPhone()); // 電話番号
    tmp.setMobilePhone(mstFloorManagement.getMobilePhone()); // 携帯番号
    tmp.setArea(String.valueOf(mstFloorManagement.getArea())); // 坪数
    tmp.setCreatedAt(mstFloorManagement.getCreatedAt());
    tmp.setUpdatedAt(mstFloorManagement.getUpdatedAt());
    tmp.setStatus(String.valueOf(mstFloorManagement.getStatus()));


    MstCode mstCode = mstCodeService.getCodeByKindAndBranch(MstCodeEnums.PREFECTURES.getValue(),
        mstFloorManagement.getPrefectures());
    tmp.setPrefectures(mstCode.getCodeName());
    return tmp;
  }

  // 表示用にエンティティからとってきたデータをフォームに格納する
  public MstFloorManagementForm selectFloorForm(MstFloorManagement mstFloorManagement) {
    MstFloorManagementForm tmp = new MstFloorManagementForm();

    tmp.setId(mstFloorManagement.getId());
    tmp.setFloorName(mstFloorManagement.getFloorName()); // 物件名

    return tmp;
  }

  public List<MstFloorName> getFloorNameAll() {
    List<MstFloorName> floorname = FloorNameService.getAllFloor();
    return floorname;
  }

  public List<MstCheckChangeRegistration> getCheckGroupAll() {
    List<MstCheckChangeRegistration> checkgroup = mstCheckChangeService.getAllGroupes();
    return checkgroup;
  }

  public List<MstEstateForm> viewEstateForm(Integer proId) {
    List<MstEstate> estates = mstEstateRepository.findAllByProId(proId);
    List<MstEstateForm> estateForms = new ArrayList<MstEstateForm>();
    for (MstEstate estate : estates) {
      MstEstateForm estateForm = new MstEstateForm();
      estateForm.setId(estate.getId());
      estateForm.setLayId(estate.getLayId());
      estateForm.setProId(proId);
      estateForm.setProName(this.getFloorId(proId).getFloorName());
      estateForm.setLayName(this.FloorNameService.getFloorId(estate.getLayId()).getFloorPlanName());
      if (estate.getCheckId() != null || !estate.getCheckId().isEmpty()) {
        estateForm.setCheckId(new ArrayList<Integer>());
        estateForm.setCheckNames(new ArrayList<String>());
        String[] splitCheck = estate.getCheckId().split(",");
        for (String c : splitCheck) {
          try {
            estateForm.getCheckId().add(Integer.parseInt(c));
            MstCheckChangeRegistration mstgroup =
                this.mstCheckChangeService.findById(Integer.parseInt(c));
            estateForm.getCheckNames().add(mstgroup.getCheckGroupName());
          } catch (NumberFormatException e) {
          }
        }
      }
      estateForms.add(estateForm);
    }

    return estateForms;
  }

  public MstFloorManagement saveStatus(Integer id) {
    MstFloorManagement floor = getFloorId(id);
    floor.setUpdatedAt(LocalDateTime.now());

    MstFloorManagement result = mstFloorManagementRepository.save(floor);
    mstFloorManagementRepository.toggleStatus(id);
    return result;
  }


  // 検索
  @Override
  public List<MstFloorManagementForm> search(MstFloorManagementForm mstFloorManagementForm) {
    String customerName = "";
    String ownerName = "";
    String floorName = "";
    String arrangement = "";
    String postCode = "";
    String prefectures = "";
    String address = "";
    String buildingName = "";
    String phone = "";
    String mobilePhone = "";
    String area = "";
    String status = "";
    String createdAt = "";
    String updatedAt = "";

    MstFloorManagementForm floor = mstFloorManagementForm;


    customerName += this.nullCheck(floor.getCustomerName());
    ownerName += this.nullCheck(floor.getOwnerName());
    floorName += this.nullCheck(floor.getFloorName());
    arrangement += this.nullCheck(floor.getArrangement());
    postCode += this.nullCheck(floor.getPostCode());
    if (floor.getPrefectures() == null || floor.getPrefectures().equals("")) {
      prefectures += "";
    } else {
      MstCode mstCode = mstCodeService.getCodeByKindAndName(floor.getPrefectures(), PREFECTURES);
      floor.setPrefectures(mstCode.getCodeBranchNum().toString());
      prefectures += floor.getPrefectures();
    }

    address += this.nullCheck(floor.getAddress());
    buildingName += this.nullCheck(floor.getBuildingName());
    phone += this.nullCheck(floor.getAddress());
    mobilePhone += this.nullCheck(floor.getMobilePhone());
    area += this.nullCheck(floor.getArea());

    if (floor.getStatus() == null || floor.getStatus().equals("")) {
      status += "";
    } else {
      if (floor.getStatus().equals("active")) {
        status += "1";
      } else {
        status += "0";
      }
    }
    createdAt += this.nullCheck(floor.getCreatedAt1());
    updatedAt += this.nullCheck(floor.getUpdatedAt1());

    List<MstFloorManagement> a = mstFloorManagementRepository.search(customerName, ownerName,
        floorName, arrangement, postCode, prefectures, address, buildingName, phone, mobilePhone,
        area, status, createdAt, updatedAt);
    List<MstFloorManagementForm> mstFloorManagementFormList = new ArrayList<>();
    for (MstFloorManagement floor1 : a) {
      mstFloorManagementFormList.add(updateFloorForm(floor1.getId()));
    }
    return mstFloorManagementFormList;
  }

  public void deleteCheckItemGroup(Integer proId, Integer layId) {
    this.mstEstateRepository.deleteByProIdAndLayId(proId, layId);
  }

  public List<MstEstate> saveAll(List<MstEstate> items) {
    return this.mstEstateRepository.saveAll(items);
  }
}
