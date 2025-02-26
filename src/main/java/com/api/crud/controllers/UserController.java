package com.api.crud.controllers;
import com.api.crud.DTO.LoginRequestDTO;
import com.api.crud.DTO.UserDTO;
import com.api.crud.DTO.UserModelDTO;
import com.api.crud.config.JWTUtil;
import com.api.crud.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping(path = "/register")
    public ResponseEntity<String> register(@RequestBody UserModelDTO userModelDto) {
        ResponseEntity<String> response;
        try {
            if (userService.findUserByEmail(userModelDto.getEmail()) != null) {
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already registered with this email");
            } else {
                this.userService.register(userModelDto);
                response = ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
            }
        } catch (IllegalArgumentException e) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data: " + e.getMessage());
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user: " + e.getMessage());
        }
        return response;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        String token = null;
        HttpStatus status = HttpStatus.OK;
        String message = null;
        try {
            token = userService.login(loginRequestDTO);
        } catch (IllegalArgumentException e) {
            status = HttpStatus.UNAUTHORIZED;
            message = e.getMessage();
        }
        return ResponseEntity.status(status).body(message != null ? message : token);
    }

}