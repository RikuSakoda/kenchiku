package com.seproject.buildmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.seproject.buildmanager.entity.MstTenant;

public interface MstTenantRepository extends JpaRepository<MstTenant, Integer> {

}
