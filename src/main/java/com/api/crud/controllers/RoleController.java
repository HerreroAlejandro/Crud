package com.api.crud.controllers;

import com.api.crud.DTO.RoleDTO;
import com.api.crud.services.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/support/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @GetMapping(path = "/ShowRoles")
    public ResponseEntity<List<RoleDTO>> getRoles() {
        logger.info("Starting to fetch roles.");
        List<RoleDTO> roles = roleService.getRoles();
        if (roles.isEmpty()) {
            logger.info("No roles found, returning empty list.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        logger.info("Roles fetched successfully. Total roles found: {}", roles.size());
        return ResponseEntity.ok(roles);
    }

    @PostMapping(path = "/SaveRole")
    public ResponseEntity<String> saveRole(@RequestBody RoleDTO roleDto) {
        logger.info("Starting to save role: {}", roleDto.getNameRole());
        ResponseEntity<String> response;
        try {
            if (roleService.findRoleByName(roleDto.getNameRole()).isPresent()) {
                logger.warn("The Role '{}' already exists.", roleDto.getNameRole());
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The Role already exists");
            } else {
                roleService.saveRole(roleDto);
                response = ResponseEntity.status(HttpStatus.CREATED).body("Role created successfully");
                logger.info("Role {} created successfully.", roleDto.getNameRole());
            }
        } catch (DataIntegrityViolationException e) {
            logger.error("DataIntegrityViolationException occurred while saving role '{}': {}", roleDto.getNameRole(), e.getMessage());
            response = ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate role name detected");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while saving role '{}': {}", roleDto.getNameRole(), e.getMessage());
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create role: " + e.getMessage());
        }
        return response;
    }
}