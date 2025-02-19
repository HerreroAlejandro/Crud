package com.api.crud.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user")
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

}
