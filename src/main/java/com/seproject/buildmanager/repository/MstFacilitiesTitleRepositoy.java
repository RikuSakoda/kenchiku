package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.seproject.buildmanager.entity.MstFacilitiesTitle;

public interface MstFacilitiesTitleRepositoy extends JpaRepository<MstFacilitiesTitle, Integer> {
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstFacilitiesTitle> findAll();

}
