package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.Service;
import com.sakute.project_fumo_backend.domain.enteties.dto.UserProfileDto;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService extends Service<User, UUID> {

    ResponseEntity<List<User>> findByName(String name);

    ResponseEntity<List<User>> findAllUsers();

    ResponseEntity<UserProfileDto> findUserpage(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

}
