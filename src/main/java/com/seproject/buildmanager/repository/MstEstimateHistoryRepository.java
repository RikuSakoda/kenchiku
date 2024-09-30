package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.seproject.buildmanager.entity.MstEstimateHistory;

public interface MstEstimateHistoryRepository extends JpaRepository<MstEstimateHistory, Integer> {
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstEstimateHistory> findAll();

  @Query(
      value = "SELECT h.id, h.estimate_id, h.update_content, h.update_user, h.update_datetime FROM mst_estimate_history h"
          + " JOIN mst_estimate e ON h.estimate_id = e.id ",
      nativeQuery = true)
  List<MstEstimateHistory> getEstimateHistory(); // 見積情報履歴テーブル

}
