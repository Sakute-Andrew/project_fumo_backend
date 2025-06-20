package com.sakute.project_fumo_backend.domain.enteties.dto;

import jakarta.validation.constraints.Max;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserProfileDto {

    private UUID id;

    private String email;

    private String userName;

    @Max(value = 238, message = "Maximum characters used")
    private String bio;

    private List<String> areasOfExpertise;

    private String profilePicture;

    private String website;

    private String location;

}
