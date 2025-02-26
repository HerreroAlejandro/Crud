package com.api.crud.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@ToString
@EqualsAndHashCode
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private long id;

    @Getter @Setter
    @NotNull (message = "Name cannot be null")
    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    @Getter @Setter
    @NotNull (message = "lastName cannot be null")
    private String lastName;

    @Column(name = "email")
    @Getter @Setter
    @Email (message = "Email should be valid")
    private String email;

    @Getter @Setter @Column(name= "phone")
    @Size (min =8, max =20)
    private String phone;

    @Getter @Setter @Column(name= "password")
    private String password;



    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Getter @Setter
    private Set<Role> roles = new HashSet<>();

    public UserModel(long id, String firstName, String lastName, String email, String phone, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public UserModel(){}




}
