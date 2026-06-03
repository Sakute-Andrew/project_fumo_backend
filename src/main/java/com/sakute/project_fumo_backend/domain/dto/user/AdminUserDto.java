package com.sakute.project_fumo_backend.domain.dto.user;

import com.sakute.project_fumo_backend.domain.enteties.user.Permission;
import com.sakute.project_fumo_backend.domain.enteties.user.Role;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDto {
    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private Set<Permission> permissions;
    private String profilePicture;
    private String bio;
    private Timestamp createdAt;

    public static AdminUserDto fromEntity(User user) {
        return AdminUserDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .permissions(user.getPermissions())
                .createdAt(user.getCreatedAt())
                .build();
    }
}