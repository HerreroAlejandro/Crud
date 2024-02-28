package com.api.crud.controllers;

import com.api.crud.models.UserModel;
import com.api.crud.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ArrayList<UserModel> getusers(){
        return this.userService.getUsers();
    }

    @PostMapping
    public UserModel saveUser(@RequestBody UserModel user){   //Requestbody significa que enviamos una request a traves del body
        return this.userService.saveUser(user);
    }

    @GetMapping(path = "/{id}")
    public Optional<UserModel> getUserById(@PathVariable("id") Long id) {
    return this.userService.getById(id);
    }

    @PutMapping(path = "/{id}")
    public UserModel updateUserById(@RequestBody UserModel Request,@PathVariable("id") Long id){
        return this.userService.updateById(Request, id);
    }

    @DeleteMapping(path = "/{id}")
    public String deleteById(@PathVariable ("id") Long id){
        boolean ok = this.userService.deleteUser(id);
    
        if (ok){
        return "User with id " + id + "deleted";
        }
        else{
         return "Error, the user cannot be deleted by id";
        }
    }


}
