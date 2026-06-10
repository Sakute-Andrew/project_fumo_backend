package com.sakute.project_fumo_backend.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @JsonProperty("username")
    @NotBlank(message = "Ім'я користувача є обов'язковим")
    @Size(min = 3, max = 30, message = "Ім'я користувача повинно бути від 3 до 30 символів")
    private String username;

    @JsonProperty("full_name")
    @NotBlank(message = "Повне ім'я є обов'язковим")
    @Size(min = 3, max = 30, message = "Повне ім'я повинно бути від 3 до 30 символів")
    private String fullName;

    @JsonProperty("email")
    @NotBlank(message = "Email є обов'язковим")
    @Email(message = "Некоректний формат email")
    private String email;

    @JsonProperty("password")
    @NotBlank(message = "Пароль є обов'язковим")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$",
            message = "Пароль повинен містити щонайменше 8 символів, великі та малі літери, цифри та спеціальні символи")
    private String password;

}
