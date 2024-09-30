package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.seproject.buildmanager.entity.MstAcceptingOrder;

public interface MstAcceptingOrderRepository extends JpaRepository<MstAcceptingOrder, Integer> {

  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstAcceptingOrder> findAll();

  @Query(
      value = "SELECT a.id, a.matter_id, a.estimate_item_id, a.order_status, a.suppliermanager_id, a.construction_start_date, a.construction_end_date, a.consumption_tax, a.key_location, a.last_updated_datetime, a.registered_user_id, a.registration_datetime, "
          + " a.subtotal, a.total FROM mst_acception_order a"
          + " JOIN mst_matter m ON a.matter_id = m.id JOIN mst_estimate_item e ON a.estimate_item_id = e.id JOIN mst_suppliermanager s ON a.suppliermanager_id = s.id where a.matter_id = :id",
      nativeQuery = true)
  List<MstAcceptingOrder> getAcceptingOrder(@Param("id") Integer id); // 発注一覧取得用

  @Query(value = "SELECT a.matter_id, MIN(a.construction_start_date) AS construction_start_date "
      + "FROM mst_acception_order a " + "GROUP BY a.matter_id", nativeQuery = true)
  List<Object[]> findOrderStartDateByMatter(); // 最速日付取得

  @Query(value = "SELECT a.matter_id, MAX(a.construction_end_date) AS construction_end_date "
      + "FROM mst_acception_order a " + "GROUP BY a.matter_id", nativeQuery = true)
  List<Object[]> findOrderEndDateByMatter(); // 最遅日付取得

  @Query(
      value = "SELECT a.id, a.matter_id, a.estimate_item_id, a.order_status, a.suppliermanager_id, s.vender_name AS supplier_name, a.construction_start_date, a.construction_end_date, a.consumption_tax, a.key_location, a.last_updated_datetime, a.registered_user_id, a.registration_datetime,"
          + "a.subtotal, a.total FROM mst_acception_order a "
          + "LEFT JOIN mst_suppliermanager s ON a.suppliermanager_id = s.id "
          + "WHERE CASE WHEN :orderStatus = '' THEN TRUE ELSE a.order_status = :orderStatus END "
          + "AND CASE WHEN :suppliermanagementName = '' THEN TRUE ELSE s.vender_name LIKE :suppliermanagementName END "
          + "AND CASE WHEN :cStart = '' THEN TRUE ELSE a.construction_start_date LIKE :cStart END "
          + "AND CASE WHEN :cEnd = '' THEN TRUE ELSE a.construction_end_date LIKE :cEnd END "
          + "AND a.matter_id = :id",
      nativeQuery = true)
  public List<MstAcceptingOrder> search(@Param("orderStatus") String orderStatus,
      @Param("suppliermanagementName") String suppliermanagementName,
      @Param("cStart") String cStart, @Param("cEnd") String cEnd, @Param("id") Integer id);


}
