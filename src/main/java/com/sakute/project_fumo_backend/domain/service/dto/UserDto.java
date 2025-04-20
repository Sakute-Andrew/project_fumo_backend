package com.sakute.project_fumo_backend.domain.service.dto;

import com.sakute.project_fumo_backend.domain.enteties.user.Role;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
public class UserDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be 3 characters min and 30 max")
    private String username;

    @NotBlank(message = "E-mail is required")
    @Email(message = "The email is invalid format")
    private String email;

    @NotBlank(message = "The password is required.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$",
            message = "Password must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters.")
    private String password;

    @NotBlank(message = "Confirmation of password is required")
    private String confirmPassword;

    private String role;

    private Timestamp lastLoginDatetime;

    private Timestamp CreatedAt;

    private Role userRole;
}
