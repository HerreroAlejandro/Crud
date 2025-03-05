package com.api.crud.controllers;

import com.api.crud.DTO.UserDTO;
import com.api.crud.DTO.UserModelDTO;
import com.api.crud.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AuthController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping(path = "/Show")
    public ResponseEntity<List<UserDTO>> getUsers() {
        logger.info("Starting to fetch users.");
        List<UserDTO> users = userService.getUsers();
        if (users.isEmpty()) {
            logger.info("No users found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        logger.info("Successfully found {} users.", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping(path = "/ShowAll")
    public ResponseEntity<Page<UserModelDTO>> getUsersModel(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        logger.info("Starting to fetch users with pagination - page: {}, size: {}, sort: {}", page, size, sort);
        String[] sortParams = sort.split(",");
        Sort.Order order = sortParams[1].equalsIgnoreCase("desc") ? Sort.Order.desc(sortParams[0]) : Sort.Order.asc(sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        Page<UserModelDTO> users = userService.getUsersModel(pageable);

        if (users.isEmpty()) {
            logger.info("No users found with the specified pagination and sorting.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
        }

        logger.info("Successfully fetched {} users (page {} of {})", users.getSize(), page, users.getTotalPages());
        return ResponseEntity.ok(users);
    }



    @GetMapping(path = "/findUserById/{id}")
    public ResponseEntity<UserModelDTO> findUserById(@PathVariable Long id) {
        logger.info("Starting to fetch user with id: {}", id);
        UserModelDTO user = userService.findUserById(id);
        if (user == null) {
            logger.info("User with ID: {} not found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("User with ID: {} found successfully.", id);
        return ResponseEntity.ok(user);
    }

    @GetMapping(path = "/findUserByEmail/{email}")
    public ResponseEntity<Object> findUserByEmail(@PathVariable String email) {
        logger.info("Starting to fetch user with email: {}", email);
        UserModelDTO userModelDTO = userService.findUserByEmail(email);
        Object response = (userModelDTO != null) ? userModelDTO
                : Map.of("message", "User with email " + email + " not found");

        if (userModelDTO == null) {
            logger.info("User with email: {} not found.", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        logger.info("User with email: {} found successfully.", email);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/findUserByName")
    public ResponseEntity<UserModelDTO> findUserByName(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("Received request to fetch user with name: {} {}", firstName, lastName);
        Optional<UserModelDTO> user = userService.findUserByName(firstName, lastName);

        if (user.isPresent()) {
            logger.info("User with name: {} {} found successfully.", firstName, lastName);
            return ResponseEntity.ok(user.get());
        }
        logger.info("User with name: {} {} not found.", firstName, lastName);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping(path = "/Alter/{id}")
    public ResponseEntity<UserModelDTO> updateUserById(@RequestBody UserModelDTO request, @PathVariable("id") Long id) {
        logger.info("Received request to update user with ID: {}", id);
        ResponseEntity<UserModelDTO> response;
        UserModelDTO updatedUser = userService.updateUserById(request, id);

        if (updatedUser != null) {
            response = ResponseEntity.ok(updatedUser);
            logger.info("User with ID: {} updated successfully.", id);
        } else {
            logger.info("User with ID: {} not found for update.", id);
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return response;
    }

    @DeleteMapping(path = "/DeleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        logger.info("Received request to delete user with ID: {}", id);
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            logger.info("User with ID: {} deleted successfully.", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        logger.info("User with ID: {} not found for deletion.", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }





}
