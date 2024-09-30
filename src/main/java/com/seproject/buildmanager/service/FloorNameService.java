package com.seproject.buildmanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.entity.MstFloorName;
import com.seproject.buildmanager.entity.MstFloorNameUpdate;
import com.seproject.buildmanager.form.MstFloorNameForm;
import com.seproject.buildmanager.repository.MstFloorNameRepository;
import com.seproject.buildmanager.repository.MstFloorNameUpdateRepository;

@Service
public class FloorNameService implements MstSearchService<MstFloorNameForm, MstFloorNameForm> {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstFloorNameRepository mstFloorNameRepository;

  private final MstFloorNameUpdateRepository mstFloorNameUpdateRepository;

  private final CommonService commonService;

  private FloorNameService(MstFloorNameRepository mstFloorNameRepository,
      MstFloorNameUpdateRepository mstFloorNameUpdateRepository, CommonService commonService) {
    this.mstFloorNameRepository = mstFloorNameRepository;
    this.mstFloorNameUpdateRepository = mstFloorNameUpdateRepository;
    this.commonService = commonService;
  }

  public List<MstFloorName> getAllFloor() {

    logger.info("--- MstFloorNameService.getAllFloor START ---");

    List<MstFloorName> floorName = mstFloorNameRepository.findAll();// リポジトリーで作成したJPAから一覧表示してエンティティに代入

    logger.info("--- MstFloorNameService.getAllFloor END ---");

    return floorName;// 代入した変数を返す
  }

  public MstFloorNameForm mstFloorNameForm() {
    MstFloorNameForm tmp = new MstFloorNameForm();
    return tmp;
  }

  public List<MstFloorNameForm> viewFloorNameForm() {

    List<MstFloorName> mstFloorName = getAllFloor();
    List<MstFloorNameForm> mstFloorNameList = new ArrayList<>();
    for (MstFloorName floorName : mstFloorName) {
      mstFloorNameList.add(updateFloorNameForm(floorName.getId()));
    }
    return mstFloorNameList;
  }

  /*
   * 表示用のメソッド
   */

  public MstFloorNameForm updateFloorNameForm(Integer id) {
    MstFloorName mstFloorName = mstFloorNameRepository.findById(id).orElse(new MstFloorName());
    MstFloorNameForm tmp = new MstFloorNameForm();
    tmp.setFloorPlanName(mstFloorName.getFloorPlanName());
    tmp.setStatus(String.valueOf(mstFloorName.getStatus()));
    tmp.setCreatedAt(mstFloorName.getCreatedAt());
    tmp.setUpdatedAt(mstFloorName.getUpdatedAt());
    tmp.setUpdatedMstUserId(mstFloorName.getUpdatedMstUserId());
    tmp.setId(mstFloorName.getId());
    return tmp;
  }



  public MstFloorName saveFloorName(String floor) {// 新規登録メソッド
    MstFloorName floorName = new MstFloorName();// エンティティインスタンス作成
    floorName.setFloorPlanName(floor);// グループ名セット
    floorName.setStatus(Constants.STATUS_TRUE);
    floorName.setCreatedAt(LocalDateTime.now());// 登録日セット
    floorName.setUpdatedAt(LocalDateTime.now());// 更新日セット※修正予定
    floorName.setUpdatedMstUserId(this.commonService.getLoginUserId());
    MstFloorName result = mstFloorNameRepository.save(floorName);// セットしたものをレポジトリーのJPAでSQLへインサート

    return result;
  }

  public MstFloorName updateFloorName(String name, Integer id) {

    logger.info("--- MstFloorNameService.updateCheckGroup START ---");

    Optional<MstFloorName> optionalGroup = mstFloorNameRepository.findById(id);
    if (!optionalGroup.isPresent()) {
      throw new IllegalArgumentException("Invalid group ID: " + id);
    }
    MstFloorName floor = optionalGroup.get();
    floor.setFloorPlanName(name);
    LocalDateTime now = LocalDateTime.now(); // 現在の日時を取得
    floor.setUpdatedAt(now);
    floor.setUpdatedMstUserId(1);
    MstFloorName floorUpdate = mstFloorNameRepository.save(floor);


    logger.info("--- MstFloorNameService.updateCheckGroup END ---");

    return floorUpdate;
  }

  public MstFloorName updateStatusFloorName(Integer id) {

    logger.info("--- MstFloorNameService.updateStatusCheckGroup START ---");

    Optional<MstFloorName> optionalFloor = mstFloorNameRepository.findById(id);
    if (!optionalFloor.isPresent()) {
      throw new IllegalArgumentException("Invalid group ID: " + id);
    }
    MstFloorName floor = optionalFloor.get();

    LocalDateTime now = LocalDateTime.now(); // 現在の日時を取得
    floor.setUpdatedAt(now);
    floor.setUpdatedMstUserId(1);
    // tmp.setUpdatedMstUserId(this.commonService.getLoginUserId());// userのidがわからないため保留
    MstFloorName floorUpdate = mstFloorNameRepository.save(floor);


    logger.info("--- MstFloorNameService.updateStatusCheckGroup END ---");

    return floorUpdate;
  }

  public MstFloorNameUpdate updateFloorName(Integer id) { // 変更テーブルへの挿入

    MstFloorName a = mstFloorNameRepository.findById(id).orElse(new MstFloorName());


    MstFloorNameUpdate mstFloorNameUpdate = new MstFloorNameUpdate();
    mstFloorNameUpdate.setUpdateId(id);
    mstFloorNameUpdate.setStatus(a.getStatus());
    LocalDateTime now = LocalDateTime.now(); // 現在の日時を取得
    mstFloorNameUpdate.setUpdatedAt(now);
    MstFloorNameUpdate result = mstFloorNameUpdateRepository.save(mstFloorNameUpdate);//
    // セットしたものをレポジトリーのJPAでSQLへインサート
    return result;
  }

  @Override
  public List<MstFloorNameForm> search(MstFloorNameForm mstFloorNameForm) {
    String status = "";
    String floorPlanName = "";
    String createdAt = "";
    String updatedAt = "";

    MstFloorNameForm floorName = mstFloorNameForm;

    floorPlanName += nullCheck(floorName.getFloorPlanName());

    if (floorName.getStatus() == null || floorName.getStatus().equals("")) {
      status += "";
    } else {
      if (floorName.getStatus().equals("active")) {
        status += "1";
      } else {
        status += "0";
      }
    }
    createdAt += nullCheck(floorName.getCreatedAt1());
    updatedAt += nullCheck(floorName.getUpdatedAt1());

    List<MstFloorName> a =
        mstFloorNameRepository.search(status, floorPlanName, createdAt, updatedAt);

    List<MstFloorNameForm> mstFloorNameFormList = new ArrayList<>();
    for (MstFloorName floorName1 : a) {
      mstFloorNameFormList.add(updateFloorNameForm(floorName1.getId()));
    }
    return mstFloorNameFormList;
  }

  public void toggleStatus(int id) {
    mstFloorNameRepository.toggleStatus(id);
  }

  public MstFloorName getFloorId(int id) {// id検索
    return mstFloorNameRepository.findById(id).orElse(new MstFloorName());
  }

  public MstFloorNameForm selectFloorForm(MstFloorName mstFloorName) {
    MstFloorNameForm tmp = new MstFloorNameForm();

    tmp.setId(mstFloorName.getId());
    tmp.setFloorPlanName(mstFloorName.getFloorPlanName()); // まどり名

    return tmp;
  }

}
