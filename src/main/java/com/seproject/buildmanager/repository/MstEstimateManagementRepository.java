package com.seproject.buildmanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.seproject.buildmanager.entity.MstEstimateManagement;

public interface MstEstimateManagementRepository
    extends JpaRepository<MstEstimateManagement, Integer> {
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstEstimateManagement> findAll();

  @Query(
      value = "SELECT e.id, e.matter_id, e.estimate_version, e.estimate_subtotal, e.accepting_orders_status ,e.estimate_consumption_tax, e.estimate_total, e.approval_subtotal, e.approval_consumption_tax, e.approval_total, e.memo, e.registration_datetime, e.latest_update_datetime FROM mst_estimate e"
          + " JOIN mst_matter m ON e.matter_id = m.id ",
      nativeQuery = true)
  List<MstEstimateManagement> getEstimate(); // 見積情報テーブル

  @Query(
      value = "SELECT e.id, e.matter_id, e.estimate_version, e.estimate_subtotal, e.accepting_orders_status ,e.estimate_consumption_tax, e.estimate_total, e.approval_subtotal, e.approval_consumption_tax, e.approval_total, e.memo, e.registration_datetime, e.latest_update_datetime FROM mst_estimate e"
          + " JOIN mst_matter m ON e.matter_id = m.id" + " WHERE e.matter_id = :matterId",
      nativeQuery = true)
  List<MstEstimateManagement> getEstimateByMatter(@Param("matterId") Integer matterId); // 見積情報テーブル

  @Query(
      value = "SELECT e.id, e.matter_id, e.estimate_version, e.estimate_subtotal, e.accepting_orders_status ,e.estimate_consumption_tax, e.estimate_total, e.approval_subtotal, e.approval_consumption_tax, e.approval_total, e.memo, e.registration_datetime, e.latest_update_datetime FROM mst_estimate e"
          + " JOIN mst_matter m ON e.matter_id = m.id"
          + " WHERE e.matter_id = :matterId AND e.estimate_version = :varsion",
      nativeQuery = true)
  Optional<MstEstimateManagement> getEstimateByMatterAndVarsion(@Param("matterId") Integer matterId,
      @Param("varsion") Integer varsion);
}
