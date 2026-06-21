package com.sakute.project_fumo_backend.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @JsonProperty("email")
    @NotEmpty(message = "Email не може бути пустим")
    @Email(message = "Email має бути правильного формату")
    private String email;

    @JsonProperty("password")
    @NotEmpty(message = "Пароль не може бути порожнім")
    private String password;

}
