package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.seproject.buildmanager.entity.MstFacilitiesDetailTitle;

public interface MstFacilitiesDetailTitleRepositoy
    extends JpaRepository<MstFacilitiesDetailTitle, Integer> {
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstFacilitiesDetailTitle> findAll();

  @Query(
      value = "SELECT d.id, d.name, d.format_id, d.status, d.preposition, d.postposition, d.registration_datetime, d.update_datetime, d.update_user "
          + "FROM mst_facilities_detail_title d LEFT JOIN mst_facilities_title_enrollments e ON d.id = e.facilities_detail_title_id "
          + "WHERE e.facilities_title_id = :facilitiesTitleId",
      nativeQuery = true)
  public List<MstFacilitiesDetailTitle> findByFacilitiesTitleId(
      @Param("facilitiesTitleId") Integer facilitiesTitleId);
}
