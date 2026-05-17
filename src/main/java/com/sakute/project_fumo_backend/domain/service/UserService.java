package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.Service;
import com.sakute.project_fumo_backend.domain.dto.user.AdminUserDto;
import com.sakute.project_fumo_backend.domain.dto.user.UserProfileDto;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;


public interface UserService extends Service<User, UUID> {

    ResponseEntity<List<User>> findByName(String name);

    ResponseEntity<UserProfileDto> findUserpage(String email);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean deleteByEmail(String email);

    ResponseEntity<AdminUserDto> updateUser(UUID id, AdminUserDto dto);

    ResponseEntity<List<AdminUserDto>> findAllUsers();

    ResponseEntity<Void> deleteById(UUID id);

}
