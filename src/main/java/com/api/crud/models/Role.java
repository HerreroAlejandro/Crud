package com.api.crud.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Table(name = "role")
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long idRole;

    @Getter @Setter
    private String nameRole;

    @ManyToMany(mappedBy = "roles")
    @Getter @Setter
    private Set<UserModel> users = new HashSet<>();

    public Role(Long idRole, String nameRole) {
        this.idRole = idRole;
        this.nameRole = nameRole;
    }

    public Role(){}

}
