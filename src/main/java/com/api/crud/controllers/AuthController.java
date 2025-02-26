package com.api.crud.controllers;

import com.api.crud.DTO.LoginRequestDTO;
import com.api.crud.DTO.UserDTO;
import com.api.crud.DTO.UserModelDTO;
import com.api.crud.config.JWTUtil;
import com.api.crud.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/Show")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getUsers();
        return users.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList())
                : ResponseEntity.ok(users);
    }

    @GetMapping(path = "/findUserById/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.findUserById(id);
        return userDTO != null ? ResponseEntity.ok(userDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(path = "/findUserByEmail/{email}")
    public ResponseEntity<Object> findUserByEmail(@PathVariable String email) {
        UserModelDTO userModelDTO = userService.findUserByEmail(email);
        Object response = (userModelDTO != null) ? userModelDTO
                : Map.of("message", "User with email " + email + " not found");

        return (userModelDTO != null) ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PutMapping(path = "/Alter/{id}")
    public ResponseEntity<UserModelDTO> updateUserById(@RequestBody UserModelDTO request, @PathVariable("id") Long id) {
        ResponseEntity<UserModelDTO> response;
        UserModelDTO updatedUser = userService.updateUserById(request, id);
        if (updatedUser != null) {
            response = ResponseEntity.ok(updatedUser);
        } else {
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return response;
    }

    @DeleteMapping(path = "/DeleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }





}
