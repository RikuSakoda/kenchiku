package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.seproject.buildmanager.entity.MstClaim;

public interface MstClaimRepository extends JpaRepository<MstClaim, Integer> {

  @EntityGraph(value = "MstClaim.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstClaim> findAll();
}
