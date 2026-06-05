package com.sakute.project_fumo_backend.domain.dto.auth;

import com.sakute.project_fumo_backend.domain.enteties.user.Permission;
import com.sakute.project_fumo_backend.domain.enteties.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class AuthUserDto {
    private UUID userId;
    private String username;
    private String email;
    private Role role;
    private Set<Permission> permissions;
}
