package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userName}")
    public ResponseEntity<?> getUserByName(@RequestParam(value = "userName") String userName) {
        return ResponseEntity.ok(userService.findByName(userName));
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
