package com.api.crud.services;

import com.api.crud.DTO.LoginRequestDTO;
import com.api.crud.DTO.RoleDTO;
import com.api.crud.DTO.UserDTO;
import com.api.crud.DTO.UserModelDTO;
import com.api.crud.config.JWTUtil;
import com.api.crud.models.Role;
import com.api.crud.models.UserModel;
import com.api.crud.repositories.RoleDao;
import com.api.crud.repositories.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JWTUtil jwtUtil;

    public List<String> getRolesForEmail(String email) {
        Optional<UserModel> response = userDao.findUserByEmail(email);

        if (response.isPresent()) {
            UserModel user = response.get();

            return user.getRoles().stream()
                    .map(role -> role.getNameRole())
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("User not found");
        }
    }


    public String login(LoginRequestDTO loginRequestDTO) {
        UserModelDTO userModelDTO = findUserByEmail(loginRequestDTO.getEmail());

        if (userModelDTO == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        } else if (!userModelDTO.getPassword().equals(loginRequestDTO.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        List<String> roles = getRolesForEmail(userModelDTO.getEmail());

        return jwtUtil.generateToken(userModelDTO.getEmail(), roles);
    }

    public List<UserDTO> getUsers() {
        List<UserModel> users = userDao.getUsers();

        List<UserDTO> userDTOs = users.stream()
                .map(userModel -> {
                    List<String> roles = userModel.getRoles().stream()
                            .map(role -> role.getNameRole())
                            .collect(Collectors.toList());

                    return new UserDTO(userModel.getId(), userModel.getFirstName(), userModel.getLastName(), userModel.getEmail(), roles);})
                .collect(Collectors.toList());
        return userDTOs;
    }

    public void register(UserModelDTO userModelDto) {

        UserModel userModel = new UserModel(userModelDto.getId(), userModelDto.getFirstName(), userModelDto.getLastName(), userModelDto.getEmail(), userModelDto.getPhone(), userModelDto.getPassword());

        Optional<RoleDTO> roleDTO = roleService.findRoleByName("CLIENT");
        if (!roleDTO.isPresent()) {
            throw new IllegalArgumentException("El rol CLIENT no existe en la base de datos");
        }
        Role role = roleDao.findRoleByName(roleDTO.get().getNameRole())
                .orElseThrow(() -> new IllegalArgumentException("El rol CLIENT  no existe en la base de datos"));

        userModel.getRoles().add(role);


        userDao.register(userModel);
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
                .map(user -> {
                    List<String> roles = user.getRoles().stream()
                            .map(role -> role.getNameRole())
                            .collect(Collectors.toList());

                    return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), roles);
                })
                .orElse(null);
    }

    public UserModelDTO findUserByEmail(String email) {
        return userDao.findUserByEmail(email)
                .map(user -> new UserModelDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getPassword()))
                .orElse(null);
    }

    public boolean deleteUser(long id) {
        return userDao.deleteUser(id);
    }

}
