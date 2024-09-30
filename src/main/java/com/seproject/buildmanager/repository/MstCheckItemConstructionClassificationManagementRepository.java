package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.seproject.buildmanager.entity.MstCheckItemConstructionClassificationManagement;

public interface MstCheckItemConstructionClassificationManagementRepository
    extends JpaRepository<MstCheckItemConstructionClassificationManagement, Integer> {


  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)

  public List<MstCheckItemConstructionClassificationManagement> findAll();



  @Query(
      value = "SELECT id, customer_id, status, check_id, construction_classification_id, constoruction_id, construction_classification_name FROM mst_check_item_construction_classification_management "
          + "WHERE customer_id = :customerId AND check_id = :checkID",
      nativeQuery = true)
  public List<MstCheckItemConstructionClassificationManagement> checkcost(
      @Param("customerId") Integer customerId, @Param("checkID") Integer checkID);


  @Query(
      value = "SELECT ck.id, ck.customer_id, ck.status, ck.check_id, ck.construction_classification_id, ck.constoruction_id, concat(c.cost_contents) construction_classification_name FROM "
          + "mst_check_item_construction_classification_management ck INNER JOIN mst_construction_classification_management c ON ck.constoruction_id = c.id WHERE customer_id = :customerId",
      nativeQuery = true)
  public List<MstCheckItemConstructionClassificationManagement> searchCosutomerId(
      @Param("customerId") Integer customerId);
}
