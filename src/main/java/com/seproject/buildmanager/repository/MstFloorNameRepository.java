package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.seproject.buildmanager.entity.MstFloorName;
import jakarta.transaction.Transactional;

public interface MstFloorNameRepository extends JpaRepository<MstFloorName, Integer> {

  public List<MstFloorName> findAll();

  @Transactional
  @Modifying
  @Query("UPDATE MstFloorName u SET u.status = CASE WHEN u.status = 1 THEN 0 ELSE 1 END WHERE u.id = :id")
  int toggleStatus(@Param("id") int id);

  @Query(value = "SELECT id,status,floor_plan_name,created_at,updated_at,updated_mst_user_id FROM"
      + " mst_floor_plan_name WHERE CASE WHEN :status = '' THEN TRUE ELSE status LIKE :status END"
      + " AND CASE WHEN :floorPlanName = '' THEN TRUE ELSE floor_plan_name LIKE :floorPlanName END"
      + " AND CASE WHEN :createdAt = '' THEN TRUE ELSE created_at LIKE :createdAt END"
      + " AND CASE WHEN :updatedAt = '' THEN TRUE ELSE updated_at LIKE :updatedAt END",
      nativeQuery = true)
  public List<MstFloorName> search(@Param("status") String status,
      @Param("floorPlanName") String floorPlanName, @Param("createdAt") String createdAt,
      @Param("updatedAt") String updatedAt);
}
