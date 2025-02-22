package com.api.crud.services;

import com.api.crud.DTO.UserDTO;
import com.api.crud.DTO.UserModelDTO;
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

    public void signUp(UserModelDTO userModelDto) {
        UserModel userModel = new UserModel(userModelDto.getId(), userModelDto.getFirstName(), userModelDto.getLastName(), userModelDto.getEmail(),userModelDto.getPhone(),userModelDto.getPassword());
        userDao.signUp(userModel);
    }

    public UserModelDTO updateUserById(UserModelDTO userModelDto, Long id) {
        UserModel userModel = new UserModel(id, userModelDto.getFirstName(), userModelDto.getLastName(), userModelDto.getEmail(), userModelDto.getPhone(), userModelDto.getPassword());
        UserModel updatedUser = userDao.updateUserById(userModel, id);

        return (updatedUser != null)
                ? new UserModelDTO(updatedUser.getId(), updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getEmail(), updatedUser.getPhone(), updatedUser.getPassword())
                : null;
    }

    public UserDTO findUserById(Long id) {
        return userDao.findUserById(id)
                .map(user -> new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()))
                .orElse(null);
    }


    public boolean deleteUser(long id) {
        return userDao.deleteUser(id);
    }


}
