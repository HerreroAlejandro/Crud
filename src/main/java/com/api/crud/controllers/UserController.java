package com.api.crud.controllers;
import com.api.crud.DTO.UserDTO;
import com.api.crud.DTO.UserModelDTO;
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

    @GetMapping(path = "/findUserById/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.findUserById(id);
        return userDTO != null ? ResponseEntity.ok(userDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping(path = "/SaveUser")
    public ResponseEntity<String> signUp(@RequestBody UserModelDTO userModelDto) {
        ResponseEntity<String> response;
        try {
            this.userService.signUp(userModelDto);
            response = ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (IllegalArgumentException e) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data: " + e.getMessage());
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user: " + e.getMessage());
        }
        return response;
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