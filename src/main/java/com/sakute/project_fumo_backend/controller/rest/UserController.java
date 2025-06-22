package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.controller.exeption.NotFoundExeption;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<List<User>> getUserList() throws NotFoundExeption {
        return userService.findAllUsers();
    }


    @GetMapping("/{userName}")
    public ResponseEntity<?> getUserByName(@PathVariable(value = "userName") String userName) throws NotFoundExeption {
        return userService.findUserpage(userName);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserById(@PathVariable(value = "userName") String userName) throws NotFoundExeption {
        return null;
    }

}
