package com.seproject.buildmanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.entity.MstOwnerManagement;

public interface MstOwnerManagementRepository extends JpaRepository<MstOwnerManagement, Integer> {
  /**
   * 全てのオーナーを取得します。
   * 
   * @return 全オーナーのリスト
   */
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstOwnerManagement> findAll();

  /**
   * オーナーのステータスを有効・無効に切り替えます。
   * 
   * <p>
   * このメソッドは、指定されたオーナーIDに対してステータスをトグルします。
   * 
   * @param userId ステータスを切り替えるオーナーのID
   */
  @Modifying
  @Transactional
  @Query("UPDATE MstOwnerManagement o SET o.status = CASE WHEN o.status = 1 THEN 0 ELSE 1 END WHERE o.id=:ownerId")
  void toggleStatus(@Param("ownerId") Integer ownerId);

  @Query(
      value = "SELECT o.id, o.client_id, c.cust_corp_name AS customer_name, o.individual, o.corporation, o.corporation_kana, o.department, o.l_name, o.f_name, o.l_name_kana, o.f_name_kana, o.post_code, o.prefectures, o.address, o.building_name, o.phone, o.mobile_phone, o.email, o.status, o.created_at, o.updated_at, o.updated_mst_user_id FROM mst_owner_management o INNER JOIN mst_customer c ON c.id = o.client_id",
      nativeQuery = true)
  public List<MstOwnerManagement> findAllWithClientName();

  @Query(
      value = "SELECT o.id, o.client_id, c.cust_corp_name AS customer_name, o.individual, o.corporation, o.corporation_kana, o.department, o.l_name, o.f_name, o.l_name_kana, o.f_name_kana, o.post_code, o.prefectures, o.address, o.building_name, o.phone, o.mobile_phone, o.email, o.status, o.created_at, o.updated_at, o.updated_mst_user_id FROM"
          + " mst_owner_management o INNER JOIN mst_customer c ON c.id = o.client_id WHERE o.id = :ownerId",
      nativeQuery = true)
  public Optional<MstOwnerManagement> findByIdWithClientName(@Param("ownerId") Integer ownerId);

  @Query(
      value = "SELECT o.id, o.client_id, c.cust_corp_name AS customer_name, o.individual, o.corporation, o.corporation_kana, o.department, o.l_name, o.f_name, o.l_name_kana, o.f_name_kana, o.post_code, o.prefectures, o.address, o.building_name, o.phone, o.mobile_phone, o.email, o.status, o.created_at, o.updated_at, o.updated_mst_user_id FROM"
          + " mst_owner_management o INNER JOIN mst_customer c ON c.id = o.client_id WHERE CASE WHEN :individual = '' THEN TRUE ELSE o.individual = :individual END AND CASE WHEN :client = '' THEN TRUE ELSE c.cust_corp_name LIKE :client END AND CASE WHEN :corporation = '' THEN TRUE ELSE o.corporation LIKE :corporation END"
          + " AND CASE WHEN :corporationKana = '' THEN TRUE ELSE o.corporation_kana LIKE :corporationKana END AND CASE WHEN :department = '' THEN TRUE ELSE o.department LIKE :department END AND CASE WHEN :ownerName = '' THEN TRUE ELSE concat(o.l_name,o.f_name) LIKE :ownerName END"
          + " AND CASE WHEN :ownerNameKana = '' THEN TRUE ELSE concat(o.l_name_kana,o.f_name_kana) LIKE :ownerNameKana END AND CASE WHEN :postCode = '' THEN TRUE ELSE o.post_code LIKE :postCode END AND CASE WHEN :prefectures = '' THEN TRUE ELSE o.prefectures = :prefectures END "
          + " AND CASE WHEN :address = '' THEN TRUE ELSE o.address LIKE :address END AND CASE WHEN :buildingName = '' THEN TRUE ELSE o.building_name LIKE :buildingName END AND CASE WHEN :phone = '' THEN TRUE ELSE o.phone LIKE :phone END"
          + " AND CASE WHEN :mobilePhone = '' THEN TRUE ELSE o.mobile_phone LIKE :mobilePhone END AND CASE WHEN :email = '' THEN TRUE ELSE o.email LIKE :email END AND CASE WHEN :status = '' THEN TRUE ELSE o.status = :status END"
          + " AND CASE WHEN :createdAt = '' THEn TRUE ELSE o.created_at LIKE :createdAt END AND CASE WHEN :updatedAt = '' THEN TRUE ELSE o.updated_at LIKE :updatedAt END",
      nativeQuery = true)
  public List<MstOwnerManagement> search(@Param("individual") String individual,
      @Param("client") String client, @Param("corporation") String corporation,
      @Param("corporationKana") String corporationKana, @Param("department") String department,
      @Param("ownerName") String ownerName, @Param("ownerNameKana") String ownerNameKana,
      @Param("postCode") String postCode, @Param("prefectures") String prefectures,
      @Param("address") String address, @Param("buildingName") String buildingName,
      @Param("phone") String phone, @Param("mobilePhone") String mobilePhone,
      @Param("email") String email, @Param("status") String status,
      @Param("createdAt") String createdAt, @Param("updatedAt") String updatedAt);
}
