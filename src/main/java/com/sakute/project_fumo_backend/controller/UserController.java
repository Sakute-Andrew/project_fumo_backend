package com.sakute.project_fumo_backend.controller;

import com.sakute.project_fumo_backend.domain.enteties.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/search/users?q={query}")
    public User getUserByName(@RequestParam String query) {
        return null;
    }

}
