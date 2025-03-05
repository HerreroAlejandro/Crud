package com.api.crud.services;

import com.api.crud.DTO.RoleDTO;
import com.api.crud.models.Role;
import com.api.crud.repositories.RoleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public Optional<RoleDTO> findRoleByName(String roleName) {
        Optional<RoleDTO> roleDTO = roleDao.findRoleByName(roleName)
                .map(role -> new RoleDTO(role.getNameRole()));

        if (roleDTO.isPresent()) {
            logger.debug("Role {} found in RoleService.", roleName);
        } else {
            logger.debug("Role {} not found in RoleService.", roleName);
        }
        return roleDTO;
    }

    public List<RoleDTO> getRoles() {
        logger.debug("Retrieving all roles from RoleService...");
        List<RoleDTO> roleDTOs = roleDao.getRoles()
                .stream()
                .map(role -> new RoleDTO(role.getNameRole()))
                .collect(Collectors.toList());

        logger.debug("Successfully retrieved {} roles from RoleService.", roleDTOs.size());
        return roleDTOs;
    }

    public void saveRole(RoleDTO roleDTO) {
        logger.info("Starting to save new role: {}", roleDTO.getNameRole());
        Role role = new Role();
        role.setNameRole(roleDTO.getNameRole());
        roleDao.save(role);
        logger.info("Role {} saved successfully in RoleService.", roleDTO.getNameRole());
    }
}
