package com.seproject.buildmanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.seproject.buildmanager.entity.MstFacilities;

public interface MstFacilitiesRepository extends JpaRepository<MstFacilities, Integer> {
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstFacilities> findAll();

  @Query(
      value = "SELECT f.id, f.facilities_management_id, facilities_title_id, f.status "
          + "FROM mst_facilities f " + "WHERE f.facilities_management_id = :managementId",
      nativeQuery = true)
  public List<MstFacilities> findByFacilitiesManagementId(
      @Param("managementId") Integer managementId);

  @Query(
      value = "SELECT f.id, f.facilities_management_id, f.facilities_title_id, f.status "
          + "FROM mst_facilities f "
          + "WHERE f.facilities_management_id = :managementId AND f.facilities_title_id = :titleId",
      nativeQuery = true)
  public Optional<MstFacilities> findByFacilitiesManagementIdAndTitleId(
      @Param("titleId") Integer titleId, @Param("managementId") Integer managementId);
}
