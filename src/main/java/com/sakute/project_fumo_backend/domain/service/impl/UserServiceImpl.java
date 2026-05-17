package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.domain.ServiceGeneric;
import com.sakute.project_fumo_backend.domain.dto.user.AdminUserDto;
import com.sakute.project_fumo_backend.domain.dto.user.UserProfileDto;
import com.sakute.project_fumo_backend.domain.dto.user.UserProfileMapper;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.UserService;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserProfilesRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl extends ServiceGeneric<User, UUID> implements UserService {

    private final UserRepository userRepository;
    private final UserProfilesRepository userProfilesRepository;
    private final UserProfileMapper userProfileMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserProfilesRepository userProfilesRepository, UserProfileMapper userProfileMapper) {
        super(userRepository);
        this.userRepository = userRepository;
        this.userProfilesRepository = userProfilesRepository;
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    public ResponseEntity<List<User>> findByName(String name) {
        return Optional.of(userRepository.findUserByUsername(name))
                .filter(list ->!list.isEmpty())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
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

    @Override
    public ResponseEntity<UserProfileDto> findUserpage(String username) throws NotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username '" + username + "' not found"));

        UserProfileDto dto = userProfileMapper.toDto(user);
        return ResponseEntity.ok(dto);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.findUserByEmail(email) != null;
    }

    @Override
    public Boolean existsByUsername(String username) {
        return !userRepository.findUserByUsername(username).isEmpty();
    }

    @Override
    public Boolean deleteByEmail(String email) {
        return !userRepository.deleteByEmail(email);
    }

}
