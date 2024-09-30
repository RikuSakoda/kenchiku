package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.seproject.buildmanager.entity.MstEstate;
import jakarta.transaction.Transactional;

public interface MstEstateRepository extends JpaRepository<MstEstate, Integer> {

  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstEstate> findAll();

  // @Query(
  // value = "SELECT e.id, e.pro_id, f.floor_name, e.lay_id, fn.floor_plan_name AS lay_name, "
  // + "GROUP_CONCAT(cr.id ORDER BY cr.id SEPARATOR ',') AS check_id, "
  // + "GROUP_CONCAT(cr.check_group_name ORDER BY cr.id SEPARATOR ',') AS check_name "
  // + "FROM mst_estate e " + "LEFT JOIN mst_floor_management f ON f.id = e.pro_id "
  // + "LEFT JOIN mst_floor_plan_name fn ON fn.id = e.lay_id "
  // + "LEFT JOIN mst_checkgroup_registration cr ON cr.id = e.check_id "
  // + "WHERE f.status = 1 AND fn.status = 1 AND cr.status = 1 "
  // + "GROUP BY e.id, e.pro_id, f.floor_name, e.lay_id, e.lay_name, e.check_id, e.check_name",
  // nativeQuery = true)

  @Query(value = "SELECT id, pro_id, lay_id, check_id FROM mst_estate WHERE pro_id" + " = :proId",
      nativeQuery = true)
  List<MstEstate> findAllByProId(@Param("proId") Integer proId);

  @Modifying
  @Transactional
  @Query("DELETE FROM MstEstate e WHERE e.proId = :proid AND e.layId = :layId")
  void deleteByProIdAndLayId(int proid, int layId);


}
