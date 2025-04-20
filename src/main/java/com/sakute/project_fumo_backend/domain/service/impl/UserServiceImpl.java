package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.ServiceGeneric;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.UserService;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    //TODO Mapping users
    @Override
    public ResponseEntity<List<User>> findAllUsers() {
         return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

}
