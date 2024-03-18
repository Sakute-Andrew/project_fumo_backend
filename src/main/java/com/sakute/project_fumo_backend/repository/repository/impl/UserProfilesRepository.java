package com.sakute.project_fumo_backend.repository.repository.impl;

import com.sakute.project_fumo_backend.domain.enteties.UserProfiles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfilesRepository extends JpaRepository<UserProfiles, Long> {
    Optional<UserProfiles> findUserProfilesByUserId(Long userId);
}
