package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualProperty;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IpStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

// IntellectualPropertyRepository.java
public interface IntellectualPropertyRepository extends JpaRepository<IntellectualProperty, UUID>, JpaSpecificationExecutor<IntellectualProperty> {

    List<IntellectualProperty> findByNameContainingIgnoreCase(String name);
    List<IntellectualProperty> findByIntellectualPropertyCategory_CategoryId(Long categoryId);
    List<IntellectualProperty> findByOwner_Username(String username);
    boolean existsByIpIdAndOwner_Username(UUID id, String username);
    List<IntellectualProperty> findByOwnerUserIdAndStatus(UUID userId, IpStatus status);

    long countByStatus(IpStatus status);
}
