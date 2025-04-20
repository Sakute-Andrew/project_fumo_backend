package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.controller.exeption.NotFoundExeption;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNullApi;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getUserList() throws NotFoundExeption {
        return userService.findAllUsers();
    }

    @GetMapping("/{userName}")
    public ResponseEntity<?> getUserByName(@PathVariable(value = "userName") String userName) throws NotFoundExeption {
        return userService.findByName(userName);
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
