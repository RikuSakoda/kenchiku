package com.seproject.buildmanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.seproject.buildmanager.entity.MstFacilitiesDetail;

public interface MstFacilitiesDetailRepository extends JpaRepository<MstFacilitiesDetail, Integer> {
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstFacilitiesDetail> findAll();

  @Query(
      value = "SELECT id, facilities_id, facilities_detail_title_id, status, value FROM mst_facilities_detail d "
          + "WHERE facilities_id=:facilitiesId",
      nativeQuery = true)
  public List<MstFacilitiesDetail> findByFacilitiesId(@Param("facilitiesId") Integer facilitiesId);

  @Query(
      value = "SELECT id, facilities_id, facilities_detail_title_id, status, value FROM mst_facilities_detail d "
          + "WHERE facilities_id=:facilitiesId AND facilities_detail_title_id = :titleId",
      nativeQuery = true)
  public Optional<MstFacilitiesDetail> findByFacilitiesIdAndTitleId(
      @Param("facilitiesId") Integer facilitiesId, @Param("titleId") Integer titleId);

  // @Query(
  // value = "SELECT id, equipment_detail_bit, name, preposition, postposition FROM
  // mst_facilities_detail",
  // nativeQuery = true)
  // public List<MstFacilitiesDetail> findFacilitiesDetailByEquipmentDetailBit(
  // @Param("facilitiesId") Integer facilitiesId);
}
