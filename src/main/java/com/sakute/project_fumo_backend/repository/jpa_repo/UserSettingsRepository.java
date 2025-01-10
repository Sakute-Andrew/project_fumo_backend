package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.UserSettings;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

import java.util.Optional;
import java.util.UUID;

public interface UserSettingsRepository extends RepositoryFactory<UserSettings, UUID> {
    Optional<UserSettings> findUserSettingsByUserId(UUID userId);
}
