package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.entity.MstAuth;

/**
 * MstAuthエンティティのためのリポジトリインターフェースです。
 * 
 * <p>
 * このインターフェースは、権限情報に対するデータベース操作を提供します。
 * JpaRepositoryインターフェースを拡張しており、標準的なCRUD操作に加え、カスタムクエリメソッドも定義できます。
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
public interface MstAuthRepository extends JpaRepository<MstAuth, Long> {

  /**
   * IDに基づいてMstAuthエンティティを取得します。
   * 
   * @param id 権限エンティティのユニーク識別子
   * @return 指定されたIDに対応するMstAuthエンティティ
   */
  MstAuth findById(Integer id);

  @Modifying
  @Transactional
  // @Query(
  // value = "UPDATE mst_owner o SET o.status = CASE WHEN o.status = 0 THEN 0 ELSE 1 END WHERE
  // o.id=:ownerId",
  // nativeQuery = true)
  @Query("UPDATE MstAuth o SET o.status = CASE WHEN o.status = 1 THEN 0 ELSE 1 END WHERE o.id=:authId")
  void toggleStatus(@Param("authId") Integer authId);

  /**
   * ステータスが指定値の全てのMstAuthエンティティを取得します。
   * 
   * @return ステータスが指定値のMstAuthエンティティのリスト
   */
  List<MstAuth> findByStatus(Integer status);


  @Modifying
  @Transactional

  @Query(
      value = "SELECT id, name, mst_auth_type_id, status, created_at, updated_at, updated_mst_user_id FROM"
          + " mst_auth WHERE CASE WHEN :Name = '' THEN TRUE ELSE name LIKE :Name END"
          + " AND CASE WHEN :mstauthtypeid = '' THEN TRUE ELSE mst_auth_type_id = :mstauthtypeid END"
          + " AND CASE WHEN :status = '' THEN TRUE ELSE status = :status END"
          + " AND CASE WHEN :createdAt = '' THEN TRUE ELSE created_at LIKE :createdAt END"
          + " AND CASE WHEN :updatedAt = '' THEN TRUE ELSE updated_at LIKE :updatedAt END",

      nativeQuery = true)
  public List<MstAuth> search(@Param("Name") String Name,
      @Param("mstauthtypeid") String mstauthtypeid, @Param("status") String status,
      @Param("createdAt") String createdAt, @Param("updatedAt") String updatedAt);

}
