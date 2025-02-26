package com.api.crud.controllers;

import com.api.crud.DTO.RoleDTO;
import com.api.crud.services.RoleService;
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

    @GetMapping(path = "/ShowRoles")
    public ResponseEntity<List<RoleDTO>> getRoles() {
        List<RoleDTO> roles = roleService.getRoles();
        return roles.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList())
                : ResponseEntity.ok(roles);
    }

    @PostMapping(path = "/SaveRole")
    public ResponseEntity<String> saveRole(@RequestBody RoleDTO roleDto) {
        ResponseEntity<String> response;
        try {
            if (roleService.findRoleByName(roleDto.getNameRole()).isPresent()) {
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The Role already exists");
            } else {
                roleService.saveRole(roleDto);
                response = ResponseEntity.status(HttpStatus.CREATED).body("Role created successfully");
            }
        } catch (DataIntegrityViolationException e) {
            response = ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate role name detected");
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create role: " + e.getMessage());
        }
        return response;
    }
}