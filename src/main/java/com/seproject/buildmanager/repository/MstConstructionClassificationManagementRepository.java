package com.seproject.buildmanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.entity.MstConstructionClassificationManagement;

public interface MstConstructionClassificationManagementRepository
    extends JpaRepository<MstConstructionClassificationManagement, Integer> {

  // @EntityGraph(value = "MstCustomer.withAllAssociations", type =
  // EntityGraph.EntityGraphType.FETCH)
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstConstructionClassificationManagement> findAll();

  @Modifying
  @Transactional
  @Query("UPDATE MstConstructionClassificationManagement o SET o.status = CASE WHEN o.status = 1 THEN 0 ELSE 1 END WHERE o.id=:costId")
  void toggleStatus(@Param("costId") Integer costId);

  @Query(
      value = "SELECT s.id AS id, s.cost_group_id, s.group_name, s.cust_id, s.cust_name, s.view_detail, s.cost_unit_id, s.cost_contents, s.cost_contents_kana, s.cost_price, s.cost_price2, s.status,s.create_at, s.updated_at, s.update_user_id "
          + "FROM mst_construction_classification_management s INNER JOIN mst_customer c ON c.id = s.cust_id "
          + "WHERE s.cost_contents = :costContents AND s.cost_group_id = :costGroupId",
      nativeQuery = true)
  public List<MstConstructionClassificationManagement> findByContents(
      @Param("costContents") String costContents, @Param("costGroupId") Integer costGroupId);

  @Query(
      value = "SELECT s.id AS id, s.cost_group_id, s.group_name, s.cust_id, s.cust_name, s.view_detail, s.cost_unit_id, s.cost_contents, s.cost_contents_kana, s.cost_price, s.cost_price2, s.status,s.create_at, s.updated_at, s.update_user_id "
          + "FROM mst_construction_classification_management s INNER JOIN mst_customer c ON c.id = s.cust_id ",
      nativeQuery = true)
  public List<MstConstructionClassificationManagement> findByCustName();

  @Query(
      value = "SELECT s.id AS id, s.cost_group_id, s.group_name, s.cust_id, s.cust_name, s.view_detail, s.cost_unit_id, s.cost_contents, s.cost_contents_kana, s.cost_price, s.cost_price2, s.status,s.create_at, s.updated_at, s.update_user_id "
          + "FROM mst_construction_classification_management s INNER JOIN mst_customer c ON c.id = s.cust_id WHERE s.id = :costId",
      nativeQuery = true)
  public Optional<MstConstructionClassificationManagement> findByIdAndGroupName(
      @Param("costId") Integer costId);

  @Query(
      value = "SELECT s.id AS id, s.cost_group_id, t.cost_group_name, s.cust_id, s.cust_name,s.view_detail,s.cost_unit_id,s.cost_contents,s.cost_contents_kana,s.cost_price,s.cost_price2,s.status,s.create_at,s.updated_at,s.update_user_id\n"
          + "FROM mst_construction_classification_management s INNER JOIN mst_constoruction t ON t.id = s.cost_group_id;",
      nativeQuery = true)
  public List<MstConstructionClassificationManagement> findByGroupName();

  @Query(
      value = "SELECT s.id, s.cost_group_id, t.cost_group_name AS group_name, s.cust_id, s.cust_name, s.view_detail, s.cost_unit_id, s.cost_contents, s.cost_contents_kana, s.cost_price, s.cost_price2, s.status, s.create_at, s.updated_at, s.update_user_id "
          + "FROM mst_construction_classification_management s "
          + "INNER JOIN mst_constoruction t ON t.id = s.cost_group_id " + "WHERE "
          + "(:custName = '' OR s.cust_name LIKE :custName) "
          + "AND (:groupName = '' OR s.group_name LIKE :groupName) "
          + "AND (:costContents = '' OR s.cost_contents LIKE :costContents) "
          + "AND (:costPrice = '' OR s.cost_price LIKE :costPrice) "
          + "AND (:costPrice2 = '' OR s.cost_price2 LIKE :costPrice2) "
          + "AND (:status = '' OR s.status = :status) "
          + "AND (:createAt = '' OR s.create_at LIKE :createAt) "
          + "AND (:updatedAt = '' OR s.updated_at LIKE :updatedAt)",
      nativeQuery = true)
  public List<MstConstructionClassificationManagement> search(@Param("custName") String custName,
      @Param("groupName") String groupName, @Param("costContents") String costContents,
      @Param("costPrice") String costPrice, @Param("costPrice2") String costPrice2,
      @Param("status") String status, @Param("createAt") String createAt,
      @Param("updatedAt") String updatedAt);


}
