package com.seproject.buildmanager.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.entity.MstCheck;
import com.seproject.buildmanager.entity.MstCheckChangeRegistration;
import com.seproject.buildmanager.entity.MstCheckItemGroupManagement;
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.repository.MstCheckItemGroupManagementRepository;

/**
 * チェック項目グループの管理を行うサービスクラスです。
 * 
 * <p>
 * このクラスは、チェック項目グループの管理に関するビジネスロジックを提供します。
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
public class CheckItemGroupManagementService {

  private final MstCustomerService mstCustomerService;
  private final MstCheckChangeService MstCheckChangeService;
  private final MstCheckService mstCheckService;
  private final MstCheckItemGroupManagementRepository mstCheckItemGroupRepository;

  // コンストラクタ
  public CheckItemGroupManagementService(MstCustomerService mstCustomerService,
      MstCheckChangeService MstCheckChangeService, MstCheckService mstCheckService,
      MstCheckItemGroupManagementRepository mstCheckItemGroupRepository) {
    this.mstCustomerService = mstCustomerService;
    this.MstCheckChangeService = MstCheckChangeService;
    this.mstCheckService = mstCheckService;
    this.mstCheckItemGroupRepository = mstCheckItemGroupRepository;
  }

  /**
   * 顧客セレクトボックスに表示する顧客を取得します。
   * 
   * @return 顧客のリスト
   */
  public List<MstCustomer> getCustmerAll() {

    List<MstCustomer> customer = this.mstCustomerService.getAllCustomers();
    return customer;

  }

  /**
   * 指定された顧客IDに紐づけられたチェック項目グループの名称を取得します。
   * 
   * @param customerId 顧客ID
   * @return チェック項目グループのリスト
   */
  public List<MstCheckChangeRegistration> getGroupNameByCustomerId(Integer customerId) {

    List<MstCheckChangeRegistration> mstCheckGroupChange =
        this.MstCheckChangeService.groupNameByCustomerId(customerId);
    return mstCheckGroupChange;

  }

  /**
   * 指定されたチェック項目グループと指定された顧客IDに紐づけられたチェック項目を取得します。
   * 
   * @param groupId チェック項目グループID
   * @param customerId 顧客ID
   * @return チェック項目のリスト
   */
  public List<MstCheck> getCheckItemByGroupIdAndCustomerId(Integer groupId, Integer customerId) {

    List<MstCheck> MstCheck =
        this.mstCheckService.CheckItemByGroupIdAndCustomerId(groupId, customerId);
    return MstCheck;

  }

  /**
   * 渡されたチェック状態を元にチェック項目とグループIDと顧客IDを紐づけるレコードの登録を行います。
   * 
   * @param items チェック項目グループのリスト
   * @return 登録されたチェック項目グループのリスト
   */
  public List<MstCheckItemGroupManagement> saveAll(List<MstCheckItemGroupManagement> items) {
    return this.mstCheckItemGroupRepository.saveAll(items);
  }

  /**
   * 指定されたグループIDと顧客IDに紐づけられたレコードを一括削除します。
   * 
   * @param groupId グループID
   * @param customerId 顧客ID
   */
  public void deleteCheckItemGroup(Integer groupId, Integer customerId) {
    this.mstCheckItemGroupRepository.deleteByGroupIdAndCustomerId(groupId, customerId);
  }
}
