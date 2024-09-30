package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.entity.MstConstruction;

public interface MstConstructionRepository extends JpaRepository<MstConstruction, Integer> {
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstConstruction> findAll();

  @Modifying
  @Transactional
  @Query("UPDATE MstConstruction u SET u.status = CASE WHEN u.status = 1 THEN 0 ELSE 1 END WHERE u.id = :constructionId")
  void toggleStatus(@Param("constructionId") Integer constructionId);

  @Query(
      value = "SELECT id, cost_group_name, status, created_at, updated_at, updated_mst_user_id FROM mst_constoruction WHERE cost_group_name = :costGroupName",
      nativeQuery = true)
  public List<MstConstruction> getConstructionByName(@Param("costGroupName") String costGroupName);


  @Query(
      value = "SELECT id, cost_group_name, status, created_at, updated_at, updated_mst_user_id FROM mst_constoruction WHERE CASE WHEN :costGroupName = '' THEN TRUE ELSE cost_group_name LIKE :costGroupName END"
          + " AND CASE WHEN :status = '' THEN TRUE ELSE status = :status END AND CASE WHEN :createdAt = '' THEN TRUE ELSE created_at LIKE :createdAt END"
          + " AND CASE WHEN :updatedAt = '' THEN TRUE ELSE updated_at LIKE :updatedAt END",
      nativeQuery = true)
  public List<MstConstruction> search(@Param("costGroupName") String costGroupName,
      @Param("status") String status, @Param("createdAt") String createdAt,
      @Param("updatedAt") String updatedAt);

}
