package com.sakute.project_fumo_backend.domain.dto.user;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  UserDto {
    private UUID id;
    private String username;
}
