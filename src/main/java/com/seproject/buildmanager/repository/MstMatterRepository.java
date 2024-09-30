package com.seproject.buildmanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.entity.MstMatter;

public interface MstMatterRepository extends JpaRepository<MstMatter, Integer> {

  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstMatter> findAll();

  @Query(
      value = "SELECT m.id, m.status, m.matter_situation_status, m.scheduled_visit_datetime, m.task_substance,"
          + "m.matter_name, m.customer_id, CONCAT(c.cust_l_name, ' ', c.cust_f_name) AS customer_name,"
          + "m.property_id,  f.floor_name AS property_name, f.address AS property_address, f.building_name AS property_building_name,"
          + "m.facility, m.registration_datetime, m.update_datetime, m.tenant_id, m.registration_user_id, m.update_user_id,"
          + "m.rental_contract_date, m.rental_contract_end_date, m.security_deposit, m.visit_id, CONCAT(u.l_name, ' ', u.f_name) AS visit_name,"
          + "m.estimate_final_version" + " FROM mst_matter m "
          + "LEFT JOIN mst_customer c ON m.customer_id = c.id "
          + "LEFT JOIN mst_floor_management f ON m.property_id = f.id "
          + "LEFT JOIN mst_tenant t ON m.tenant_id = t.id "
          + "LEFT JOIN mst_user u ON m.visit_id = u.id ",
      nativeQuery = true)
  List<MstMatter> findDisplay();

  @Query(
      value = "SELECT m.id, m.status, m.matter_situation_status, m.scheduled_visit_datetime, m.task_substance,"
          + "m.matter_name, m.customer_id, CONCAT(c.cust_l_name, ' ', c.cust_f_name) AS customer_name,"
          + "m.property_id,  f.floor_name AS property_name, f.address AS property_address, f.building_name AS property_building_name,"
          + "m.facility, m.registration_datetime, m.update_datetime, m.tenant_id, m.registration_user_id, m.update_user_id,"
          + "m.rental_contract_date, m.rental_contract_end_date, m.security_deposit, m.visit_id, CONCAT(u.l_name, ' ', u.f_name) AS visit_name,"
          + "m.estimate_final_version" + " FROM mst_matter m "
          + "LEFT JOIN mst_customer c ON m.customer_id = c.id "
          + "LEFT JOIN mst_floor_management f ON m.property_id = f.id "
          + "LEFT JOIN mst_tenant t ON m.tenant_id = t.id "
          + "LEFT JOIN mst_user u ON m.visit_id = u.id WHERE m.id = :id",
      nativeQuery = true)
  Optional<MstMatter> findDisplayById(@Param("id") Integer id);

  @Query(
      value = "SELECT m.id, m.status, m.matter_situation_status, m.scheduled_visit_datetime, m.task_substance,"
          + "m.matter_name, m.customer_id, CONCAT(c.cust_l_name, c.cust_f_name) AS customer_name,"
          + "m.property_id,  f.floor_name AS property_name, f.address AS property_address, f.building_name AS property_building_name,"
          + "m.facility, m.registration_datetime, m.update_datetime, m.tenant_id, m.registration_user_id, m.update_user_id,"
          + "m.rental_contract_date, m.rental_contract_end_date, m.security_deposit, m.visit_id, CONCAT(u.l_name, ' ', u.f_name) AS visit_name,"
          + "m.estimate_final_version" + " FROM mst_matter m "
          + "LEFT JOIN mst_customer c ON m.customer_id = c.id "
          + "LEFT JOIN mst_floor_management f ON m.property_id = f.id "
          + "LEFT JOIN mst_tenant t ON m.tenant_id = t.id "
          + "LEFT JOIN mst_user u ON m.visit_id = u.id "
          + "WHERE CASE WHEN :situationStatus = '' THEN TRUE ELSE m.matter_situation_status = :situationStatus END AND CASE WHEN :taskSubstance = '' THEN TRUE ELSE m.task_substance = :taskSubstance END "
          + "AND CASE WHEN :visit = '' THEN TRUE ELSE m.scheduled_visit_datetime LIKE :visit END AND CASE WHEN :matterName = '' THEN TRUE ELSE m.matter_name LIKE :matterName END "
          + "AND CASE WHEN :customerName = '' THEN TRUE ELSE CONCAT(c.cust_l_name, c.cust_f_name) LIKE :customerName END AND CASE WHEN :propertyName = '' THEN TRUE ELSE f.floor_name LIKE :propertyName END "
          + "AND CASE WHEN :propertyAddress = '' THEN TRUE ELSE f.address LIKE :propertyAddress END AND CASE WHEN :propertyBuildingName = '' THEN TRUE ELSE f.building_name LIKE :propertyBuildingName END "
          + "AND CASE WHEN :facility = '' THEN TRUE ELSE m.facility = :facility END AND CASE WHEN :createdAt1 = '' THEN TRUE ELSE m.registration_datetime LIKE :createdAt1 END "
          + "AND CASE WHEN :updatedAt1 = '' THEN TRUE ELSE m.update_datetime LIKE :updatedAt1 END",
      nativeQuery = true)
  public List<MstMatter> search(@Param("situationStatus") String situationStatus,
      @Param("taskSubstance") String taskSubstance, @Param("visit") String visit,
      @Param("matterName") String matterName, @Param("customerName") String customerName,
      @Param("propertyName") String propertyName, @Param("propertyAddress") String propertyAddress,
      @Param("propertyBuildingName") String propertyBuildingName,
      @Param("facility") String facility, @Param("createdAt1") String createdAt1,
      @Param("updatedAt1") String updatedAt1);

  @Modifying
  @Transactional
  @Query("UPDATE MstMatter SET status = CASE WHEN status = 1 THEN 0 ELSE 1 END WHERE id = :id")
  void toggleStatus(@Param("id") Integer id);

  @Modifying
  @Transactional
  @Query("UPDATE MstMatter SET facility = true WHERE id = :id")
  void updateFicilties(@Param("id") Integer id);


}
