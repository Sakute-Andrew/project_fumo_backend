package com.sakute.project_fumo_backend.repository.jpa_repo.intprop;

import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualProperty;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IntellectualPropertyRepository  extends JpaRepository<IntellectualProperty, UUID> {

    List<IntellectualProperty> findByNameContainingIgnoreCase(String name);

    List<IntellectualProperty> findByOwner_Username(String username);

    List<IntellectualProperty> findByIntellectualPropertyCategory_CategoryId(Long categoryId);

    boolean existsByIpIdAndOwner_Username(UUID ipId, String username);

    @Query("SELECT ip FROM IntellectualProperty ip WHERE ip.status = :status")
    Page<IntellectualProperty> findAllApproved(@Param("status") IpStatus status, Pageable pageable);

    @Query("SELECT ip FROM IntellectualProperty ip WHERE ip.typeIp = :type")
    List<IntellectualProperty> findByType(@Param("type") String type);
}
