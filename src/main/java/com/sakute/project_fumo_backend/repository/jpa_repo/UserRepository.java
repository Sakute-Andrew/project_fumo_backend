package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.User;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends RepositoryFactory<User, UUID> {
    Optional<User> findUserByUserId(UUID userId);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUsername(String username);




}
