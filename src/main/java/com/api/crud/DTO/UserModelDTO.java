package com.api.crud.DTO;

import lombok.Getter;
import lombok.Setter;

public class UserModelDTO {

    @Getter
    @Setter
    private long id;

    @Getter @Setter
    private String firstName;

    @Getter @Setter
    private String lastName;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String phone;

    @Getter @Setter
    private String password;

    public UserModelDTO(long id, String firstName, String lastName, String email, String phone, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public UserModelDTO(){}
}
