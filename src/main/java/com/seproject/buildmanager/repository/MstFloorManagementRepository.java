package com.seproject.buildmanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.entity.MstFloorManagement;

public interface MstFloorManagementRepository extends JpaRepository<MstFloorManagement, Integer> {

  @Query(value = "SELECT " + "    fm.id," + "    fm.customer_id," + "    fm.owner_id,"
      + "    fm.floor_name," + "    fm.arrangement," + "    fm.rent," + "   fm.security_deposit,"
      + "    fm.post_code," + "    fm.prefectures," + "    fm.address," + "    fm.building_name,"
      + "    fm.phone," + "    fm.mobile_phone," + "    fm.area," + "    fm.status,"
      + "    fm.created_at," + "    fm.updated_at," + "    fm.updated_mst_user_id,"
      + "    c.cust_corp_name AS customer_name,"
      + "    CONCAT(o.l_name, ' ', o.f_name) AS owner_name" + " FROM"
      + "    mst_floor_management fm" + " LEFT JOIN" + "    mst_customer c" + " ON "
      + "    fm.customer_id = c.id" + " LEFT JOIN" + "    mst_owner_management o" + " ON "
      + "    fm.owner_id = o.id", nativeQuery = true)
  List<MstFloorManagement> findDisplay();

  @Query(
      value = "SELECT fm.id AS id, fm.customer_id, fm.owner_id, fm.floor_name, fm.arrangement, fm.rent, fm.security_deposit, fm.post_code, fm.prefectures, fm.address, fm.building_name, fm.phone, fm.mobile_phone, fm.area, fm.status, fm.created_at, fm.updated_at, fm.updated_mst_user_id, "
          + "c.cust_corp_name AS customer_name,"
          + "CONCAT(o.l_name, ' ', o.f_name) AS owner_name FROM mst_floor_management fm LEFT JOIN mst_customer c ON fm.customer_id = c.id  LEFT JOIN mst_owner_management o ON fm.owner_id = o.id WHERE fm.id = :floorId",
      nativeQuery = true)
  public Optional<MstFloorManagement> findByIdAndCustomerName(@Param("floorId") Integer floorId);

  boolean existsByFloorName(String floorName);



  @Modifying
  @Transactional
  @Query("UPDATE MstFloorManagement fm SET fm.status = CASE WHEN fm.status = 1 THEN 0 ELSE 1 END WHERE fm.id = :floorId")
  void toggleStatus(@Param("floorId") Integer floorId);

  @Query(
      value = "SELECT fm.id, fm.customer_id, fm.owner_id, fm.floor_name, fm.arrangement, fm.rent, fm.security_deposit, fm.post_code, fm.prefectures, fm.address, fm.building_name, fm.phone, fm.mobile_phone, fm.area, fm.status, fm.created_at, fm.updated_at, fm.updated_mst_user_id, c.cust_corp_name AS customer_name, "
          + "CONCAT(o.l_name, ' ', o.f_name) AS owner_name FROM mst_floor_management fm "
          + "INNER JOIN mst_customer c ON c.id = fm.customer_id INNER JOIN mst_owner o ON o.id = fm.owner_id "
          + "WHERE (:customerName = '' OR c.cust_corp_name LIKE :customerName) "
          + "AND (:ownerName = '' OR CONCAT(o.l_name, ' ', o.f_name) LIKE :ownerName) "
          + "AND (:floorName = '' OR fm.floor_name LIKE :floorName) "
          + "AND (:arrangement = '' OR fm.arrangement LIKE :arrangement)"
          + "AND (:postCode = '' OR fm.post_code LIKE :postCode) "
          + "AND (:prefectures = '' OR fm.prefectures = :prefectures) "
          + "AND (:address = '' OR fm.address LIKE :address) "
          + "AND (:buildingName = '' OR fm.building_name LIKE :buildingName) "
          + "AND (:phone = '' OR fm.phone LIKE :phone) "
          + "AND (:mobilePhone = '' OR fm.mobile_phone LIKE :mobilePhone) "
          + "AND (:area = '' OR fm.area LIKE :area) " + "AND (:status = '' OR fm.status = :status) "
          + "AND (:createdAt = '' OR fm.created_at LIKE :createdAt) "
          + "AND (:updatedAt = '' OR fm.updated_at LIKE :updatedAt)",
      nativeQuery = true)
  public List<MstFloorManagement> search(@Param("customerName") String customerName,
      @Param("ownerName") String ownerName, @Param("floorName") String floorName,
      @Param("arrangement") String arrangement, @Param("postCode") String postCode,
      @Param("prefectures") String prefectures, @Param("address") String address,
      @Param("buildingName") String buildingName, @Param("phone") String phone,
      @Param("mobilePhone") String mobilePhone, @Param("area") String area,
      @Param("status") String status, @Param("createdAt") String createdAt,
      @Param("updatedAt") String updatedAt);

}
