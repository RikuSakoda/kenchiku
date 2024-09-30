package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.seproject.buildmanager.entity.MstAcceptingOrderDetail;

public interface MstAcceptingOrderDetailRepository
    extends JpaRepository<MstAcceptingOrderDetail, Integer> {

  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstAcceptingOrderDetail> findAll();

  @Query(
      value = "SELECT d.id, d.order_id, d.matter_id, d.estimate_item_id, d.content, d.manufacturer_name, d.model_number, d.unit, d.unit_price, d.volume, d.subtotal, d.note, d.registered_user_id, d.registration_datetime, d.last_updated_datetime FROM mst_acception_order_detail d"
          + " JOIN mst_matter m ON d.matter_id = m.id JOIN mst_estimate_item e ON d.estimate_item_id = e.id JOIN mst_acception_order a ON d.order_id = a.id where d.order_id = :id",
      nativeQuery = true)
  List<MstAcceptingOrderDetail> getAcceptingOrderDetail(@Param("id") Integer id); // 発注詳細取得用


}
