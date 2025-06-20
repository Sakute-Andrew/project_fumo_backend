package com.sakute.project_fumo_backend.domain.enteties.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sakute.project_fumo_backend.domain.enteties.user.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class AuthUserDto {
    @JsonProperty("userId")
    private UUID id;
    @JsonProperty("username")
    private String username;
    @Email
    @JsonProperty("email")
    private String email;
    @JsonProperty("role")
    private Role userRole;
}
