package com.api.crud.services;

import com.api.crud.DTO.UserDTO;
import com.api.crud.models.UserModel;
import com.api.crud.repositories.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public List<UserDTO> getUsers() {
        List<UserModel> users = userDao.getUsers();
        return users.stream()
                .map(UserModel -> new UserDTO(UserModel.getId(), UserModel.getFirstName(), UserModel.getLastName(), UserModel.getEmail()))
                .collect(Collectors.toList());
    }


    public void signUp(UserModel user) {
        userDao.signUp(user);
    }

    public UserDTO findUserById(Long id) {
        return userDao.findUserById(id)
                .map(user -> new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()))
                .orElse(null);
    }

    public UserModel findUserModelById(Long id) {
        return userDao.findUserById(id).orElse(null);
    }

    public UserModel updateUserById(UserModel request, Long id) {
        return userDao.updateUserById(request, id);
    }

    public void deleteUser(long id) {
        userDao.deleteUser(id);
    }


}
