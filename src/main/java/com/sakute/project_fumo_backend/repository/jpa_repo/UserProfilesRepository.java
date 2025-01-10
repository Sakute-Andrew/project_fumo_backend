package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.User;
import com.sakute.project_fumo_backend.domain.enteties.UserProfiles;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

import java.util.Optional;

public interface UserProfilesRepository extends RepositoryFactory<UserProfiles, Long> {
    Optional<UserProfiles> findUserProfilesByUserId(User userId);
}
