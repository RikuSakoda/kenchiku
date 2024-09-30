package com.seproject.buildmanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.entity.MstSupplierManagement;

public interface MstSupplierManagementRepository
    extends JpaRepository<MstSupplierManagement, Integer> {

  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstSupplierManagement> findAll();

  @Query(
      value = "SELECT s.id AS id, s.classification, s.vender_name, s.post_code, s.prefectures, s.address, s.building_name, s.phone, s.fax_num1, s.fax_num2, s.fax_num3, s.email, s.department, s.staff_name, s.created_at, s.updated_at, s.updated_mst_user_id "
          + "FROM mst_suppliermanager s",
      nativeQuery = true)
  Optional<MstSupplierManagement> findByLoginCd(@Param("loginCd") String loginCd);

  @Modifying
  @Transactional
  @Query("UPDATE MstSupplierManagement o SET o.status = CASE WHEN o.status = 1 THEN 0 ELSE 1 END WHERE o.id=:supplierId")
  void toggleStatus(@Param("supplierId") Integer supplierId);

  @Query(
      value = "SELECT s.id, s.classification, s.vender_name, s.post_code, s.prefectures, s.address, s.building_name, s.phone, s.fax_num1, s.email, s.department, s.staff_name, s.created_at, s.updated_at, s.status, s.updated_mst_user_id FROM"
          + " mst_suppliermanager s WHERE "
          + "CASE WHEN :classification = '' THEN TRUE ELSE s.classification = :classification END"
          + " AND CASE WHEN :venderName = '' THEN TRUE ELSE s.vender_name LIKE :venderName END"
          + " AND CASE WHEN :postCode = '' THEN TRUE ELSE s.post_code LIKE :postCode END"
          + " AND CASE WHEN :prefectures = '' THEN TRUE ELSE s.prefectures = :prefectures END"
          + " AND CASE WHEN :buildingName = '' THEN TRUE ELSE s.building_name LIKE :buildingName END"
          + " AND CASE WHEN :address = '' THEN TRUE ELSE s.address LIKE :address END"
          + " AND CASE WHEN :faxNum1 = '' THEN TRUE ELSE s.fax_num1 LIKE :faxNum1 END"
          + " AND CASE WHEN :email = '' THEN TRUE ELSE s.email LIKE :email END"
          + " AND CASE WHEN :department = '' THEN TRUE ELSE s.department LIKE :department END"
          + " AND CASE WHEN :staffName = '' THEN TRUE ELSE s.staff_name LIKE :staffName END"
          + " AND CASE WHEN :status = '' THEN TRUE ELSE s.status LIKE :status END"
          + " AND CASE WHEN :createdAt = '' THEN TRUE ELSE s.created_at LIKE :createdAt END"
          + " AND CASE WHEN :updatedAt = '' THEN TRUE ELSE s.updated_at LIKE :updatedAt END"
          + " AND CASE WHEN :phone = '' THEN TRUE ELSE s.phone LIKE :phone END"
          + " AND CASE WHEN :updatedMstUserId = '' THEN TRUE ELSE s.updated_mst_user_id = :updatedMstUserId END",
      nativeQuery = true)
  public List<MstSupplierManagement> search(@Param("classification") String classification,
      @Param("venderName") String venderName, @Param("postCode") String postCode,
      @Param("prefectures") String prefectures, @Param("address") String address,
      @Param("buildingName") String buildingName, @Param("phone") String phone,
      @Param("faxNum1") String faxNum1, @Param("email") String email,
      @Param("department") String department, @Param("staffName") String staffName,
      @Param("status") String status, @Param("createdAt") String createdAt,
      @Param("updatedAt") String updatedAt, @Param("updatedMstUserId") String updatedMstUserId);



}
