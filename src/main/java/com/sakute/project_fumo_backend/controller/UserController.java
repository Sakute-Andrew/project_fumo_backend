package com.sakute.project_fumo_backend.controller;

import com.sakute.project_fumo_backend.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users?q={query}")
    public ResponseEntity<?> getUserByName(@RequestParam String query) {
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String name, @RequestParam String password) {
        return null;
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String name, @RequestParam String password) {
        return null;
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestParam String name) {
        return null;
    }

    @PostMapping("/settings/profile")
    public ResponseEntity<?> updateUser(@RequestParam String name, @RequestParam String password) {
        return null;
    }

    @PostMapping("/settings/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam String name, @RequestParam String password) {
        return null;
    }

    @DeleteMapping("/settings/delete-account")
    public ResponseEntity<?> deleteUser(@RequestParam String name) {
        return null;
    }

}
