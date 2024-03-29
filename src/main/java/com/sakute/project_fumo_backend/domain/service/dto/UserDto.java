package com.sakute.project_fumo_backend.domain.service.dto;

import com.sakute.project_fumo_backend.domain.enteties.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
public class UserDto {
    @NotNull
    private Long id;
    private String username;
    private String email;
    private String role;
    private Timestamp lastLoginDatetime;
    private Timestamp CreatedAt;
    private Role userRole;
}
