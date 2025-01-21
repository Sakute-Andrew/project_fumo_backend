package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.Service;
import com.sakute.project_fumo_backend.domain.enteties.User;

import java.util.Optional;
import java.util.UUID;


public interface UserService extends Service<User, UUID> {

    Optional<User> findByName(String name);

}
