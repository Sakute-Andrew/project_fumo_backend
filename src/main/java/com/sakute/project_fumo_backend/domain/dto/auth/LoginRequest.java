package com.sakute.project_fumo_backend.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    @JsonProperty("email")
    @NotEmpty
    @Email
    private String email;

    @JsonProperty("password")
    @NotEmpty
    private String password;
}
