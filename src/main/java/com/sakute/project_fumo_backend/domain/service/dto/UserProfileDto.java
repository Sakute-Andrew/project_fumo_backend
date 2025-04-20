package com.sakute.project_fumo_backend.domain.service.dto;

import jakarta.validation.constraints.Max;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileDto {

    private UserDto user;

    @Max(value = 238, message = "Maximum characters used")
    private String bio;

    private List<String> areasOfExpertise;

    private String profilePicture;

    private String website;

    private String location;

}
