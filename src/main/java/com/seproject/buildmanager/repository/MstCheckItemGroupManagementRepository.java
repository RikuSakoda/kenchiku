package com.seproject.buildmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.seproject.buildmanager.entity.MstCheckItemGroupManagement;
import jakarta.transaction.Transactional;

/**
 * MstCheckItemGroupエンティティのためのリポジトリインタフェースです。
 * 
 * <p>
 * このインタフェースは、チェック項目グループに関するCRUD操作およびカスタムクエリを提供します。
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
public interface MstCheckItemGroupManagementRepository
    extends JpaRepository<MstCheckItemGroupManagement, Integer> {

  /**
   * 指定されたグループIDと顧客IDに基づいてMstCheckItemGroupエンティティを削除します。
   * 
   * @param groupId グループID
   * @param customerId 顧客ID
   */
  @Modifying
  @Transactional
  @Query("DELETE FROM MstCheckItemGroupManagement c WHERE c.groupId = :groupId AND c.customerId = :customerId")
  void deleteByGroupIdAndCustomerId(int groupId, int customerId);

}
