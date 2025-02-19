package com.api.crud.DTO;

import com.api.crud.models.UserModel;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

public class UserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long id;

    @Getter @Setter
    private String fullName;

    @Getter @Setter
    private String email;

    public UserDTO(long id, String firstName, String lastName, String email){
        this.id=id;
        this.fullName=firstName + " " + lastName;
        this.email=email;
    }


}