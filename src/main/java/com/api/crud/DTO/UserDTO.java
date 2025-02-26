package com.api.crud.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UserDTO {

    @Getter @Setter
    private long id;

    @Getter @Setter
    private String firstName;

    @Getter @Setter
    private String lastName;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private List<String> roles;


    public UserDTO(long id, String firstName, String lastName, String email, List<String> roles){
        this.id=id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.roles=roles;
    }
    public UserDTO(){}


}