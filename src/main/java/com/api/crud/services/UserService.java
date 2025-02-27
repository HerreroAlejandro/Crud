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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

                    return new UserDTO(userModel.getId(), userModel.getFirstName(), userModel.getLastName(), userModel.getEmail());
                })
                .collect(Collectors.toList());
        return userDTOs;
    }

    public Page<UserModelDTO> getUsersModel(Pageable pageable) {
        Page<UserModel> users = userDao.getUsersModel(pageable);

        return users.map(userModel -> {
            List<String> roles = userModel.getRoles().stream()
                    .map(role -> role.getNameRole())
                    .collect(Collectors.toList());

            return new UserModelDTO(userModel.getId(), userModel.getFirstName(), userModel.getLastName(), userModel.getEmail(), userModel.getPassword(), userModel.getPhone(), roles);
        });
    }

    public UserModelDTO updateUserById(UserModelDTO userModelDto, Long id) {
        Optional<UserModel> userOptional = userDao.findUserById(id);

        if (!userOptional.isPresent()) {
            return null;
        }

        UserModel user = userOptional.get();

        user.setFirstName(userModelDto.getFirstName());
        user.setLastName(userModelDto.getLastName());
        user.setEmail(userModelDto.getEmail());
        user.setPhone(userModelDto.getPhone());
        user.setPassword(userModelDto.getPassword());

        UserModel updatedUser = userDao.updateUserById(user, id);

        return (updatedUser != null)
                ? new UserModelDTO(updatedUser.getId(), updatedUser.getFirstName(), updatedUser.getLastName(),
                updatedUser.getEmail(), updatedUser.getPhone(), updatedUser.getPassword(),
                updatedUser.getRoles().stream()
                        .map(Role::getNameRole)
                        .collect(Collectors.toList()))
                : null;
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


    public UserModelDTO findUserById(Long id) {
        return userDao.findUserById(id)
                .map(user -> {
                    List<String> roles = user.getRoles().stream()
                            .map(role -> role.getNameRole())
                            .collect(Collectors.toList());

                    return new UserModelDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getPassword(), roles);
                })
                .orElse(null);
    }

    public UserModelDTO findUserByEmail(String email) {
        return userDao.findUserByEmail(email)
                .map(user -> new UserModelDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getPassword(), user.getRoles()
                        .stream()
                        .map(Role::getNameRole)
                        .collect(Collectors.toList())))
                .orElse(null);
    }

    public Optional<UserModelDTO> findUserByName(String firstName, String lastName) {
        Optional<UserModel> user = userDao.findUserByName(firstName, lastName);

        return user.map(userModel -> {
            List<String> roles = userModel.getRoles().stream()
                    .map(role -> role.getNameRole())
                    .collect(Collectors.toList());

            return new UserModelDTO(userModel.getId(), userModel.getFirstName(), userModel.getLastName(), userModel.getEmail(), userModel.getPhone(), userModel.getPassword(), roles);
        });
    }


    public boolean deleteUser(long id) {
        return userDao.deleteUser(id);
    }

}
