package com.sakute.project_fumo_backend.domain.dto;

import com.sakute.project_fumo_backend.domain.enteties.RequestStatus;
import com.sakute.project_fumo_backend.domain.enteties.user.Permission;
import lombok.Data;
import java.sql.Timestamp;
import java.util.UUID;

@Data
public class PermissionRequestDto {
    private UUID id;
    private UUID userId;
    private String username;
    private Permission requestedPermission;
    private RequestStatus status;
    private String message;
    private Timestamp createdAt;
}