package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.seproject.buildmanager.entity.MstDestination;

public interface MstDestinationRepository extends JpaRepository<MstDestination, Integer>{
  
  public List<MstDestination> findAll();

}
