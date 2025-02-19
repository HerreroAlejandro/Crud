package com.api.crud.controllers;

import com.api.crud.DTO.UserDTO;
import com.api.crud.models.UserModel;
import com.api.crud.services.DTOService;
import com.api.crud.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DTOService DTOService;

    @GetMapping(path = "/Show")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getUsers();
        return users.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList())
                : ResponseEntity.ok(users);
    }

    @PostMapping(path = "/SaveUser")
    public ResponseEntity<String> signUp(@RequestBody UserModel user) {
        ResponseEntity<String> response;
        try {
            this.userService.signUp(user);
            response = ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (IllegalArgumentException e) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data: " + e.getMessage());
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user: " + e.getMessage());
        }
        return response;
    }

    @GetMapping(path = "/findUserById/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.findUserById(id);
        return userDTO != null ? ResponseEntity.ok(userDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping(path = "/Alter/{id}")
    public ResponseEntity<UserModel> updateUserById(@RequestBody UserModel request, @PathVariable("id") Long id) {
        ResponseEntity<UserModel> result;

        UserModel updatedUser = userService.updateUserById(request, id);
        if (updatedUser != null) {
            result = ResponseEntity.ok(updatedUser);
        } else {
            result = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return result;
    }

    @DeleteMapping(path = "/DeleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        ResponseEntity<Void> result;
        UserModel user = userService.findUserModelById(id);
        if (user != null) {
            userService.deleteUser(id);
            result = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            result = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return result;
    }


}
