package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.controller.exeption.NotFoundExeption;
import com.sakute.project_fumo_backend.domain.ServiceGeneric;
import com.sakute.project_fumo_backend.domain.enteties.dto.UserProfileDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.mapper.UserProfileMapper;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.UserProfileService;
import com.sakute.project_fumo_backend.domain.service.UserService;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserProfilesRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    //TODO Mapping users
    @Override
    public ResponseEntity<List<User>> findAllUsers() {
         return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserProfileDto> findUserpage(String username) throws NotFoundExeption {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundExeption("User with username '" + username + "' not found"));

        UserProfileDto dto = userProfileMapper.toDto(user);
        return ResponseEntity.ok(dto);
    }

    @Override
    public Boolean existsByEmail(String email) {
        if (userRepository.findUserByEmail(email) == null){
            return false;
        }
        return true;
    }

    @Override
    public Boolean existsByUsername(String username) {
        if (userRepository.findUserByUsername(username).isEmpty()){
            return false;
        }
        return true;
    }

}
