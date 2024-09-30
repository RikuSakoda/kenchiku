package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.seproject.buildmanager.entity.MstAcceptingOrderHistory;

public interface MstAcceptingOrderHistoryRepository
    extends JpaRepository<MstAcceptingOrderHistory, Integer> {

  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstAcceptingOrderHistory> findAll();



}
