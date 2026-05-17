package com.sakute.project_fumo_backend.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @JsonProperty("refreshToken")
    private String refreshToken;
}
