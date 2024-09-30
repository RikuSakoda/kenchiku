package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.entity.MstMaterialsManagement;

public interface MstMaterialsManagementRepository
    extends JpaRepository<MstMaterialsManagement, Integer> {
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  // @EntityGraph(value = "MstCheck.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstMaterialsManagement> findAll();

  @Modifying
  @Transactional
  @Query("UPDATE MstMaterialsManagement u SET u.status = CASE WHEN u.status = 1 THEN 0 ELSE 1 END WHERE u.id = :materialsId")
  void toggleStatus(@Param("materialsId") Integer materialsId);

  @Query(
      value = "SELECT id, materials_name, status, created_at, updated_at, updated_mst_user_id FROM"
          + " mst_materials_management WHERE CASE WHEN :materialsName = '' THEN TRUE ELSE materials_name LIKE :materialsName END AND CASE WHEN :status = '' THEN TRUE ELSE status = :status END"
          + " AND CASE WHEN :createdAt = '' THEN TRUE ELSE created_at LIKE :createdAt END AND CASE WHEN :updatedAt = '' THEN TRUE ELSE updated_at LIKE :updatedAt END",
      nativeQuery = true)
  public List<MstMaterialsManagement> search(@Param("materialsName") String materialsName,
      @Param("status") String status, @Param("createdAt") String createdAt,
      @Param("updatedAt") String updatedAt);
}
