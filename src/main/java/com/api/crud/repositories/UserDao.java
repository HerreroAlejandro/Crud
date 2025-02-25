package com.api.crud.repositories;

import com.api.crud.models.UserModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<UserModel> getUsers();

    boolean deleteUser(long id);

    void register(UserModel user);

    @Query("SELECT u FROM UserModel u WHERE u.id = :id")
    Optional<UserModel> findUserById(@Param("id") Long id);

    Optional<UserModel> findUserByEmail(String email);

   // public Optional<UserModel> findUserByName(String firstName);

    public UserModel updateUserById(UserModel Request, Long id);



}