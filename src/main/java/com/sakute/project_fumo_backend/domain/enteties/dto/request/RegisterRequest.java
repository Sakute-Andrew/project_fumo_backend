package com.sakute.project_fumo_backend.domain.enteties.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    @JsonProperty("username")
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be 3 characters min and 30 max")
    private String username;

    @JsonProperty("full_name")
    @NotBlank(message = "Fullname is required")
    @Size(min = 3, max = 30, message = "Fullname must be 3 characters min and 30 max")
    private String fullName;

    @JsonProperty("email")
    @NotBlank(message = "E-mail is required")
    @Email(message = "The email is invalid format")
    private String email;

    @JsonProperty("password")
    @NotBlank(message = "The password is required.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$",
            message = "Password must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters.")
    private String password;
}
