package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.seproject.buildmanager.entity.MstEstimateItem;

public interface MstEstimateItemRepository extends JpaRepository<MstEstimateItem, Integer> {
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstEstimateItem> findAll();

  @Query(
      // value = "SELECT i.id, i.estimate_id,i.matter_id, i.construction_id,
      // i.construction_classification_id, "
      // + "cc.cost_unit_id AS unit, i.volume, cc.cost_price2 AS estimate_unit_price,
      // i.estimate_amount, cc.cost_price AS approval_unit_price,"
      // + "cc.cost_contents AS construction_classification_name, c.cost_group_name AS
      // construction_name,"
      // + "i.tenant_burden_ratio, i.tenant_burden_amount, i.tenant_amount, i.note,
      // i.registration_datetime, i.latest_update_datetime "
      // + "FROM mst_estimate_item i JOIN mst_construction_classification_management cc ON
      // i.construction_classification_id = cc.id "
      // + "JOIN mst_constoruction c ON i.construction_id = c.id "
      // + "JOIN mst_estimate e ON i.estimate_id = e.id",
      value = "SELECT i.id, i.estimate_id,i.matter_id, i.construction_id, i.construction_classification_id, "
          + "cc.cost_unit_id AS unit, i.volume, cc.cost_price2 AS estimate_unit_price, i.estimate_amount, cc.cost_price AS approval_unit_price,"
          + "i.construction_classification_name, i.construction_name,"
          + "i.tenant_burden_ratio, i.tenant_burden_amount, i.tenant_amount, i.note, i.registration_datetime, i.latest_update_datetime "
          + "FROM mst_estimate_item i JOIN mst_construction_classification_management cc ON i.construction_classification_id = cc.id "
          + "JOIN mst_constoruction c ON i.construction_id = c.id "
          + "JOIN mst_estimate e ON i.estimate_id = e.id",
      nativeQuery = true)
  List<MstEstimateItem> getEstimateItem(); // 見積情報項目テーブル

  @Query(
      // value = "SELECT i.id, i.estimate_id,i.matter_id, i.construction_id,
      // i.construction_classification_id, "
      // + "cc.cost_unit_id AS unit, i.volume, cc.cost_price2 AS estimate_unit_price,
      // i.estimate_amount, cc.cost_price AS approval_unit_price,"
      // + "cc.cost_contents AS construction_classification_name, c.cost_group_name AS
      // construction_name,"
      // + "i.tenant_burden_ratio, i.tenant_burden_amount, i.tenant_amount, i.note,
      // i.registration_datetime, i.latest_update_datetime, e.estimate_version "
      // + "FROM mst_estimate_item i JOIN mst_construction_classification_management cc ON
      // i.construction_classification_id = cc.id "
      // + "JOIN mst_constoruction c ON i.construction_id = c.id "
      // + "JOIN mst_matter m ON i.matter_id = m.id "
      // + "JOIN mst_estimate e ON i.estimate_id = e.id"
      // + " where i.matter_id = :matterId AND e.estimate_version = :estimateVersion",
      value = "SELECT i.id, i.estimate_id,i.matter_id, i.construction_id, i.construction_classification_id, "
          + "cc.cost_unit_id AS unit, i.volume, cc.cost_price2 AS estimate_unit_price, i.estimate_amount, cc.cost_price AS approval_unit_price,"
          + "i.construction_classification_name, i.construction_name,"
          + "i.tenant_burden_ratio, i.tenant_burden_amount, i.tenant_amount, i.note, i.registration_datetime, i.latest_update_datetime, e.estimate_version "
          + "FROM mst_estimate_item i JOIN mst_construction_classification_management cc ON i.construction_classification_id = cc.id "
          + "JOIN mst_constoruction c ON i.construction_id = c.id "
          + "JOIN mst_matter m ON i.matter_id = m.id "
          + "JOIN mst_estimate e ON i.estimate_id = e.id"
          + " where i.matter_id = :matterId AND e.estimate_version = :estimateVersion",
      nativeQuery = true)
  List<MstEstimateItem> getEstimateItemVerId(@Param("matterId") Integer matterId,
      @Param("estimateVersion") Integer estimateVersion); // 見積情報項目テーブル ID検索

  @Query(
      // value = "SELECT i.id, i.estimate_id,i.matter_id, i.construction_id,
      // i.construction_classification_id, "
      // + "cc.cost_unit_id AS unit, i.volume, i.estimate_unit_price, i.estimate_amount,
      // cc.cost_price AS approval_unit_price,"
      // + "cc.cost_contents AS construction_classification_name, c.cost_group_name AS
      // construction_name,"
      // + "i.tenant_burden_ratio, i.tenant_burden_amount, i.tenant_amount, i.note,
      // i.registration_datetime, i.latest_update_datetime, e.estimate_version "
      // + "FROM mst_estimate_item i JOIN mst_construction_classification_management cc ON
      // i.construction_classification_id = cc.id "
      // + "JOIN mst_constoruction c ON i.construction_id = c.id "
      // + "JOIN mst_matter m ON i.matter_id = m.id "
      // + "JOIN mst_estimate e ON i.estimate_id = e.id"
      // + " where i.matter_id = :matterId AND e.estimate_version = :estimateVersion",
      value = "SELECT i.id, i.estimate_id,i.matter_id, i.construction_id, i.construction_classification_id, "
          + "cc.cost_unit_id AS unit, i.volume, i.estimate_unit_price, i.estimate_amount, cc.cost_price AS approval_unit_price,"
          + "i.construction_classification_name, i.construction_name,"
          + "i.tenant_burden_ratio, i.tenant_burden_amount, i.tenant_amount, i.note, i.registration_datetime, i.latest_update_datetime, e.estimate_version "
          + "FROM mst_estimate_item i JOIN mst_construction_classification_management cc ON i.construction_classification_id = cc.id "
          + "JOIN mst_constoruction c ON i.construction_id = c.id "
          + "JOIN mst_matter m ON i.matter_id = m.id "
          + "JOIN mst_estimate e ON i.estimate_id = e.id"
          + " where i.matter_id = :matterId AND e.estimate_version = :estimateVersion",
      nativeQuery = true)
  List<MstEstimateItem> getAllEstimateItemVerId(@Param("matterId") Integer matterId,
      @Param("estimateVersion") Integer estimateVersion); // 見積情報項目テーブル ID検索



  @Query(
      // value = "SELECT i.id, i.estimate_id,i.matter_id, i.construction_id,
      // i.construction_classification_id, "
      // + "cc.cost_unit_id AS unit, i.volume, cc.cost_price2 AS estimate_unit_price,
      // i.estimate_amount, cc.cost_price AS approval_unit_price,"
      // + "cc.cost_contents AS construction_classification_name, c.cost_group_name AS
      // construction_name,"
      // + "i.tenant_burden_ratio, i.tenant_burden_amount, i.tenant_amount, i.note,
      // i.registration_datetime, i.latest_update_datetime, e.estimate_version "
      // + "FROM mst_estimate_item i JOIN mst_construction_classification_management cc ON
      // i.construction_classification_id = cc.id "
      // + "JOIN mst_constoruction c ON i.construction_id = c.id "
      // + "JOIN mst_matter m ON i.matter_id = m.id "
      // + "JOIN mst_estimate e ON i.estimate_id = e.id" + " where i.matter_id = :matterId",
      value = "SELECT i.id, i.estimate_id,i.matter_id, i.construction_id, i.construction_classification_id, "
          + "cc.cost_unit_id AS unit, i.volume, cc.cost_price2 AS estimate_unit_price, i.estimate_amount, cc.cost_price AS approval_unit_price,"
          + "i.construction_classification_name, i.construction_name,"
          + "i.tenant_burden_ratio, i.tenant_burden_amount, i.tenant_amount, i.note, i.registration_datetime, i.latest_update_datetime, e.estimate_version "
          + "FROM mst_estimate_item i JOIN mst_construction_classification_management cc ON i.construction_classification_id = cc.id "
          + "JOIN mst_constoruction c ON i.construction_id = c.id "
          + "JOIN mst_matter m ON i.matter_id = m.id "
          + "JOIN mst_estimate e ON i.estimate_id = e.id" + " where i.matter_id = :matterId",


      nativeQuery = true)
  List<MstEstimateItem> getEstimateItemById(@Param("matterId") Integer matterId); // 見積情報項目テーブル ID検索

}
