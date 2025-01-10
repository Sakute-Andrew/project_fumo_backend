package com.sakute.project_fumo_backend.domain.service.dto;

import lombok.Data;

@Data
public class UserProfileDto {

    private String username;
    private String bio;
    private String areasOfExpertise;
    private String profilePicture;
    private String website;
    private String location;

}
