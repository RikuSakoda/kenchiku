package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.entity.MstAuthType;

public interface MstAuthTypeRepository extends JpaRepository<MstAuthType, Integer> {
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  // @EntityGraph(value = "MstCheck.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstAuthType> findAll();

  @Modifying
  @Transactional
  @Query("UPDATE MstAuthType a SET a.status = CASE WHEN a.status = 1 THEN 0 ELSE 1 END WHERE a.id = :authtypeId")
  void toggleStatus(@Param("authtypeId") Integer authtypeId);

  @Query(value = "SELECT id, auth_name, status, created_at, updated_at, updated_mst_user_id FROM"
      + " mst_auth_type WHERE CASE WHEN :authName = '' THEN TRUE ELSE auth_name LIKE :authName END AND CASE WHEN :status = '' THEN TRUE ELSE status = :status END"
      + " AND CASE WHEN :createdAt = '' THEN TRUE ELSE created_at LIKE :createdAt END AND CASE WHEN :updatedAt = '' THEN TRUE ELSE updated_at LIKE :updatedAt END",
      nativeQuery = true)
  public List<MstAuthType> search(@Param("authName") String authName,
      @Param("status") String status, @Param("createdAt") String createdAt,
      @Param("updatedAt") String updatedAt);
}
