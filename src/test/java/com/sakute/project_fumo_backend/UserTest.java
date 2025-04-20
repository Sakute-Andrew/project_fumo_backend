package com.sakute.project_fumo_backend;

import com.github.javafaker.Faker;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@SpringBootTest
public class UserTest {

    @Autowired
    UserService userService;

    @Test
    public void createUser() {
        User user = new User();
        Faker faker = new Faker();

        user.setUserId(UUID.randomUUID());
        user.setUsername(faker.name().username());
        user.setEmail(faker.internet().emailAddress());
        user.setUserRole(1L);
        user.setLastLoginDatetime(Timestamp.from(Instant.now()));
        user.setCreatedAt(Timestamp.from(Instant.now()));
        userService.save(user);
    }


    @Test
    public void findUserByUsername() {

    }


}
