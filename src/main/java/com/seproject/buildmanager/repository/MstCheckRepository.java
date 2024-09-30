package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.entity.MstCheck;

/**
 * MstCheckエンティティのためのリポジトリインタフェースです。
 * 
 * <p>
 * このインタフェースは、チェック項目に関するCRUD操作およびカスタムクエリを提供します。
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
public interface MstCheckRepository extends JpaRepository<MstCheck, Integer> {

  /**
   * 全てのチェック項目を取得します。
   * 
   * @return チェック項目のリスト
   */
  @EntityGraph(value = "MstCheck.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstCheck> findAll();

  /**
   * 特定のチェック項目のステータスをトグルします。
   * 
   * @param checkId チェック項目のID
   */
  @Modifying
  @Transactional
  @Query("UPDATE MstCheck c SET c.status = CASE WHEN c.status = 1 THEN 0 ELSE 1 END WHERE c.id = :checkId")
  void toggleStatus(@Param("checkId") Integer checkId);

  /**
   * 指定されたグループIDおよび顧客IDに紐づけられたチェック項目を取得します。
   * 
   * @param groupId グループID
   * @param customerId 顧客ID
   * @return チェック項目のリスト
   */
  @Query(value = "SELECT "
      + "    mc.id, mc.check_detail as check_detail, mc.status as status, mc.created_at as created_at, mc.updated_at as updated_at, mc.updated_mst_user_id as updated_mst_user_id, mcig.group_id, mcig.check_id, mcig.customer_id "
      + " FROM mst_check AS mc LEFT OUTER JOIN mst_check_item_group_management AS mcig ON (mc.id = mcig.check_id) AND mcig.customer_id = :customerId"
      + " AND mcig.group_id = :groupId", nativeQuery = true)
  List<MstCheck> getCheckItemByGroupIdAndCustomerId(@Param("groupId") Integer groupId,
      @Param("customerId") Integer customerId);

  @Query(value = "SELECT id, check_detail, status, created_at, updated_at, updated_mst_user_id FROM"
      + " mst_check WHERE CASE WHEN :checkDetail = '' THEN TRUE ELSE check_detail LIKE :checkDetail END AND CASE WHEN :status = '' THEN TRUE ELSE status = :status END "
      + "AND CASE WHEN :createdAt = '' THEN TRUE ELSE created_at LIKE :createdAt END AND CASE WHEN :updatedAt = '' THEN TRUE ELSE updated_at LIKE :updatedAt END",
      nativeQuery = true)
  public List<MstCheck> search(@Param("checkDetail") String checkDetail,
      @Param("status") String status, @Param("createdAt") String createdAt,
      @Param("updatedAt") String updatedAt);
}
