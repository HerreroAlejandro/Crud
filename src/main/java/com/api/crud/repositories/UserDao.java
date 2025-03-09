package com.api.crud.repositories;

import com.api.crud.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<UserModel> getUsers();

    Page<UserModel> getUsersModel(Pageable pageable);

    boolean deleteUserById(long id);

    boolean deleteUserByEmail(String email);

    void register(UserModel user);

    @Query("SELECT u FROM UserModel u WHERE u.id = :id")
    Optional<UserModel> findUserById(@Param("id") Long id);

    Optional<UserModel> findUserByEmail(String email);

    public Optional<UserModel> findUserByName(String firstName, String lastName);

    public UserModel updateUserById(UserModel Request, Long id);



}