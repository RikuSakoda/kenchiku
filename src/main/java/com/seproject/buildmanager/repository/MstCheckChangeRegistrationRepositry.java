package com.seproject.buildmanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.entity.MstCheckChangeRegistration;

/**
 * MstCheckGroupRegistrationエンティティのためのリポジトリインタフェースです。
 * 
 * <p>
 * このインタフェースは、チェックグループ登録に関するCRUD操作およびカスタムクエリを提供します。
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
@Repository
public interface MstCheckChangeRegistrationRepositry
    extends JpaRepository<MstCheckChangeRegistration, Integer> {

  /**
   * 全てのチェックグループ登録を取得します。
   * 
   * @return チェックグループ登録のリスト
   */
  public List<MstCheckChangeRegistration> findAll();

  /**
   * 特定のチェックグループ登録のステータスをトグルします。
   * 
   * @param id チェックグループ登録のID
   * @return 更新された行数
   */
  @Transactional(rollbackFor = Exception.class)
  @Modifying
  @Query("UPDATE MstCheckChangeRegistration u SET u.status = CASE WHEN u.status = 1 THEN 0 ELSE 1 END WHERE u.id = :id")
  int toggleStatus(@Param("id") int id);

  /**
   * 指定されたIDのチェックグループ登録を取得します。
   * 
   * @param id チェックグループ登録のID
   * @return 指定されたIDのチェックグループ登録
   */
  public Optional<MstCheckChangeRegistration> findById(@Param("id") Integer id);

  /**
   * 検索条件に基づいてチェックグループ登録を検索します。
   * 
   * @param checkGroupName チェックグループ名
   * @param status ステータス
   * @param createdAt 作成日
   * @param updatedAt 更新日
   * @return 検索結果のリスト
   */
  @Query(
      value = "SELECT id, check_group_name, status, created_at, updated_at, updated_mst_user_id,check_detail FROM"
          + " mst_check_group WHERE CASE WHEN :checkGroupName = '' THEN TRUE ELSE check_group_name LIKE :checkGroupName END AND CASE WHEN :status = '' THEN TRUE ELSE status = :status END"
          + " AND CASE WHEN :createdAt = '' THEN TRUE ELSE created_at LIKE :createdAt END AND CASE WHEN :updatedAt = '' THEN TRUE ELSE updated_at LIKE :updatedAt END",
      nativeQuery = true)
  public List<MstCheckChangeRegistration> search(@Param("checkGroupName") String checkGroupName,
      @Param("status") String status, @Param("createdAt") String createdAt,
      @Param("updatedAt") String updatedAt);

  /**
   * 指定された顧客IDに紐づけられたチェックグループの名前を取得します。
   * 
   * @param customerId 顧客ID
   * @return チェックグループ登録のリスト
   */
  @Query(value = "SELECT " + "    mcr.id AS id, " + "    mcr.check_group_name AS check_group_name, "
      + "    mcr.status AS status, " + "    mcr.created_at AS created_at, "
      + "    mcr.updated_at AS updated_at, " + "    mcr.updated_mst_user_id AS updated_mst_user_id "
      + ", GROUP_CONCAT(mc.check_detail) AS check_detail " + " FROM mst_check_group AS mcr "
      + " LEFT OUTER JOIN mst_check_item_group_management AS mcig "
      + "   ON mcr.id = mcig.group_id AND mcig.customer_id = :customerId "
      + " LEFT OUTER JOIN mst_check AS mc " + "   ON mcig.check_id = mc.id "
      + " GROUP BY mcr.id, mcr.check_group_name, mcr.status, mcr.created_at, mcr.updated_at, mcr.updated_mst_user_id ",
      nativeQuery = true)
  public List<MstCheckChangeRegistration> getGroupNameByCustomerId(
      @Param("customerId") Integer customerId);

}
