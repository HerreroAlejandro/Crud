package com.api.crud.repositories;

import com.api.crud.models.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleDao {

    @Query("SELECT r FROM Role r WHERE r.nameRole = :roleName")
    Optional<Role> findRoleByName(@Param("roleName") String roleName);

    void save(Role role);

    List<Role> getRoles();
}