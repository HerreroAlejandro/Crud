package com.api.crud.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity //Indica que cada campo que tenga la clase va a ser una columna en la bd
@Table(name = "user")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private long id;

    @Column(name = "firstName")
    @Getter @Setter
    private String firstName;

    @Column(name = "lastName")
    @Getter @Setter
    private String lastName;

    @Column(name = "email")
    @Getter @Setter
    private String email;




}
