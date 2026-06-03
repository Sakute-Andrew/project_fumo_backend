package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.Service;
import com.sakute.project_fumo_backend.domain.dto.user.AdminUserDto;
import com.sakute.project_fumo_backend.domain.enteties.user.Permission;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;
import java.util.UUID;


public interface UserService extends Service<User, UUID> {

    ResponseEntity<AdminUserDto> updateUser(UUID id, AdminUserDto dto);

    ResponseEntity<List<AdminUserDto>> findAllUsers();

    ResponseEntity<Void> deleteById(UUID id);

    void updatePermissions(UUID userId, Set<Permission> permissions);

    ResponseEntity<List<User>> findByName(String name);

}
