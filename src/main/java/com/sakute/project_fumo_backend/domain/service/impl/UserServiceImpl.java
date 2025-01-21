package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.ServiceGeneric;
import com.sakute.project_fumo_backend.domain.enteties.User;
import com.sakute.project_fumo_backend.domain.service.UserService;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Optional<User> findByName(String name) {
        return userRepository.findUserByUsername(name);
    }
}
