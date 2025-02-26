package com.api.crud.services;

import com.api.crud.DTO.RoleDTO;
import com.api.crud.models.Role;
import com.api.crud.repositories.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public Optional<RoleDTO> findRoleByName(String roleName) {
        return roleDao.findRoleByName(roleName)
                .map(role -> new RoleDTO(role.getNameRole()));
    }

    public List<RoleDTO> getRoles() {
        return roleDao.getRoles()
                .stream()
                .map(role -> new RoleDTO(role.getNameRole()))
                .collect(Collectors.toList());
    }

    public void saveRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setNameRole(roleDTO.getNameRole());
        roleDao.save(role);
    }
}
