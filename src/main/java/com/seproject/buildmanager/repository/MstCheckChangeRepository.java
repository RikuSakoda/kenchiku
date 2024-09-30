package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.seproject.buildmanager.entity.MstCheckChange;

@Repository
public interface MstCheckChangeRepository
    extends JpaRepository<MstCheckChange, Integer> {
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)

  public List<MstCheckChange> findAll();
}
