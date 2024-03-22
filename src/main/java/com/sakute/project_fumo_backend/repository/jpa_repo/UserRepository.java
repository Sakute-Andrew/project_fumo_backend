package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserId(Long userId);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUsername(String username);




}
