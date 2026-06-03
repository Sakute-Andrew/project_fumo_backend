package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.domain.ServiceGeneric;
import com.sakute.project_fumo_backend.domain.dto.user.AdminUserDto;
import com.sakute.project_fumo_backend.domain.enteties.user.Permission;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.UserService;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl extends ServiceGeneric<User, UUID> implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<List<User>> findByName(String name) {
        return Optional.of(userRepository.findUserByUsername(name))
                .filter(list ->!list.isEmpty())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Override
    public void updatePermissions(UUID userId, Set<Permission> permissions) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setPermissions(permissions);
        userRepository.save(user);
    }

    @Transactional
    //TODO Mapping users
    @Override
    public ResponseEntity<List<AdminUserDto>> findAllUsers() {
        return ResponseEntity.ok(
                userRepository.findAll()
                        .stream()
                        .map(AdminUserDto::fromEntity)
                        .toList()
        );
    }


    public ResponseEntity<Void> deleteById(UUID id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<AdminUserDto> updateUser(UUID id, AdminUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setRole(dto.getRole());
        user.setPermissions(dto.getPermissions());
        return ResponseEntity.ok(AdminUserDto.fromEntity(userRepository.save(user)));
    }


}
