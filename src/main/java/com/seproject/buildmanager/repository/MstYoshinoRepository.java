package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.entity.MstYoshino;

public interface MstYoshinoRepository extends JpaRepository<MstYoshino, Integer> {

  public List<MstYoshino> findAll();
  
  /**
   * オーナーのステータスを有効・無効に切り替えます。
   * 
   * <p>
   * このメソッドは、指定されたオーナーIDに対してステータスをトグルします。
   * 
   * @param userId ステータスを切り替えるオーナーのID
   */
  
  @Modifying
  @Transactional
  @Query("UPDATE MstYoshino o SET o.status = CASE WHEN o.status = 1 THEN 0 ELSE 1 END WHERE o.id=:YoshinoId")
  void toggleStatus(@Param("YoshinoId") Integer YoshinoId);
  
}