package com.api.crud.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity //Indica que cada campo que tenga la clase va a ser una columna en la bd
@Table(name = "user")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private long id;

    @Column(name = "firstName")
    @Getter @Setter
    @NotEmpty(message = "El campo nombre no puede ser vacio")
    @Max(value = 50, message = "El nombre no puede tener mas de 50 caracteres")
    private String firstName;

    @Column(name = "lastName")
    @Getter @Setter
    @NotEmpty(message = "El campo apellido no puede ser vacio")
    @Max (value = 50, message = "El apellido no puede tener menos de 3 caracteres o mas de 50")
    private String lastName;

    @Column(name = "email")
    @Getter @Setter
    @Email
    private String email;




}
