package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.PermissionRequest;
import com.sakute.project_fumo_backend.domain.enteties.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRequestRepository extends JpaRepository<PermissionRequest, UUID> {
    // Перевірка, чи немає вже активної заявки від цього юзера
    @EntityGraph(attributePaths = {"user"})
    Optional<PermissionRequest> findById(UUID id);

    @EntityGraph(attributePaths = {"user"})
    Page<PermissionRequest> findAll(Pageable pageable);

    boolean existsByUser_UserIdAndRequestedPermissionAndStatus(UUID userId, com.sakute.project_fumo_backend.domain.enteties.user.Permission permission, RequestStatus status);


    long countByStatus(RequestStatus pending);
}