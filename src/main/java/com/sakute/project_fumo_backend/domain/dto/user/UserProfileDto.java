package com.sakute.project_fumo_backend.domain.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserProfileDto {

    private UUID id;

    @Email(message = "Некоректний формат email")
    private String email;

    private String userName;

    @Size(max = 238, message = "Maximum characters used") // Виправлено
    private String bio;

    private List<String> areasOfExpertise;

    private String profilePicture;
    private String website;
    private String location;
}