package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.seproject.buildmanager.entity.MstEstimateItemHistory;

public interface MstEstimateItemHistoryRepository
    extends JpaRepository<MstEstimateItemHistory, Integer> {
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstEstimateItemHistory> findAll();

  @Query(
      value = "SELECT ih.id, ih.estimte_item_id, ih.update_content, ih.update_user, ih.update_datetime FROM mst_estimate_item_history ih"
          + " JOIN mst_estimate_item i ON ih.estimate_item_id = i.id",
      nativeQuery = true)
  List<MstEstimateItemHistory> getEstimateItemHistory(); // 見積情報項目履歴テーブル


}
