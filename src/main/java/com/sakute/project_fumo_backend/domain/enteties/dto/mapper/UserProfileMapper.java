package com.sakute.project_fumo_backend.domain.enteties.dto.mapper;

import com.sakute.project_fumo_backend.domain.enteties.dto.UserProfileDto;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.enteties.user.UserProfiles;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserProfileMapper {

    public UserProfileDto toDto(User user) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setUserName(user.getUsername());

        // Мапінг профайлу, якщо він існує
        if (user.getUserProfile() != null) {
            UserProfiles profile = user.getUserProfile();
            dto.setBio(profile.getBio());
            dto.setWebsite(profile.getWebsite());
            dto.setLocation(profile.getLocation());

            // Розбиваємо areas_of_expertise на список
            if (profile.getAreasOfExpertise() != null) {
                dto.setAreasOfExpertise(
                        Arrays.asList(profile.getAreasOfExpertise().split(","))
                );
            }
        }

        return dto;
    }
}
