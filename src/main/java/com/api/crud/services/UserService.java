package com.api.crud.services;

import com.api.crud.DTO.LoginRequestDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void register(UserModelDTO userModelDto) {
        logger.info("Starting to process register users in service");

        UserModel userModel = new UserModel(userModelDto.getId(), userModelDto.getFirstName(), userModelDto.getLastName(),
                userModelDto.getEmail(), userModelDto.getPhone(), userModelDto.getPassword());

        Role role = roleDao.findRoleByName("CLIENT")
                .orElseThrow(() -> {
                    logger.debug("Registration failed: Role CLIENT does not exist");
                    return new IllegalArgumentException("El rol CLIENT no existe en la base de datos");
                });

        userModel.getRoles().add(role);
        userDao.register(userModel);
        logger.info("User {} registered successfully", userModelDto.getEmail());
    }

    public List<UserDTO> getUsers() {
        logger.info("Starting to process getusers in service");
        List<UserModel> users = userDao.getUsers();

        if (users.isEmpty()) {
            logger.debug("No users found in service.");
        }
        List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()))
                .collect(Collectors.toList());
        logger.info("Successfully retrieved {} users", users.size());
        return userDTOs;
    }


    public String login(LoginRequestDTO loginRequestDTO) {
        logger.info("Attempting Service login for user: {}", loginRequestDTO.getEmail());
        UserModelDTO userModelDTO = findUserByEmail(loginRequestDTO.getEmail());

        if (userModelDTO == null) {
            logger.debug("Login failed: User {} not found", loginRequestDTO.getEmail());
            throw new IllegalArgumentException("User not found");
        } else if (!userModelDTO.getPassword().equals(loginRequestDTO.getPassword())) {
            logger.debug("Login failed: Incorrect password for {}", loginRequestDTO.getPassword());
            throw new IllegalArgumentException("Wrong password");
        }

        List<String> roles = getRolesForEmail(userModelDTO.getEmail());
        logger.info("User {} logged in successfully with roles {}", loginRequestDTO.getEmail(), roles);
        return jwtUtil.generateToken(userModelDTO.getEmail(), roles);
    }



    public Page<UserModelDTO> getUsersModel(Pageable pageable) {
        logger.info("Starting to process getusersModel in service...");
        Page<UserModel> users = userDao.getUsersModel(pageable);

        Page<UserModelDTO> userDTOs = users.map(userModel ->
                new UserModelDTO(userModel.getId(), userModel.getFirstName(), userModel.getLastName(), userModel.getEmail(), userModel.getPassword(), userModel.getPhone(),
                        userModel.getRoles().stream().map(Role::getNameRole).collect(Collectors.toList())));

        logger.info("Successfully retrieved {} users", userDTOs.getTotalElements());
        return userDTOs;
    }

    public UserModelDTO updateUserById(UserModelDTO userModelDto, Long id) {
        logger.info("Starting to process Update user with ID: {}", id);
        Optional<UserModel> userOptional = userDao.findUserById(id);

        if (!userOptional.isPresent()) {
            logger.debug("Update failed: User with ID {} not found", id);
            return null;
        }

        UserModel user = userOptional.get();
        user.setFirstName(userModelDto.getFirstName());
        user.setLastName(userModelDto.getLastName());
        user.setEmail(userModelDto.getEmail());
        user.setPhone(userModelDto.getPhone());
        user.setPassword(userModelDto.getPassword());

        UserModel updatedUser = userDao.updateUserById(user, id);
        logger.info("User with ID {} updated successfully", id);

        return (updatedUser != null)
                ? new UserModelDTO(updatedUser.getId(), updatedUser.getFirstName(), updatedUser.getLastName(),
                updatedUser.getEmail(), updatedUser.getPhone(), updatedUser.getPassword(),
                updatedUser.getRoles().stream().map(Role::getNameRole).collect(Collectors.toList()))
                : null;
    }

    public UserModelDTO findUserById(Long id) {
        logger.info("Starting to process Search for user with ID: {}", id);
        return userDao.findUserById(id)
                .map(user -> {
                    logger.info("User with ID {} found", id);
                    return new UserModelDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                            user.getPhone(), user.getPassword(), user.getRoles().stream().map(Role::getNameRole).collect(Collectors.toList()));
                })
                .orElseGet(() -> {
                    logger.debug("User with ID {} not found", id);
                    return null;
                });
    }

    public UserModelDTO findUserByEmail(String email) {
        logger.info("Starting to process Search for user with email: {}", email);

        return userDao.findUserByEmail(email)
                .map(user -> {
                    logger.info("User found: {}", user.getEmail());
                    return new UserModelDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getPassword(), user.getRoles()
                            .stream()
                            .map(Role::getNameRole)
                            .collect(Collectors.toList()));
                })
                .orElseGet(() -> {
                    logger.debug("User not found with email: {}", email);
                    return null;
                });
    }

    public Optional<UserModelDTO> findUserByName(String firstName, String lastName) {
        logger.info("Starting to process Search for user with name: {} and lastName: {}", firstName, lastName);
        Optional<UserModel> user = userDao.findUserByName(firstName, lastName);

        return Optional.ofNullable(user.map(userModel -> {
            logger.info("User found: {} {}", userModel.getFirstName(), userModel.getLastName());
            List<String> roles = userModel.getRoles().stream()
                    .map(role -> role.getNameRole())
                    .collect(Collectors.toList());

            return new UserModelDTO(userModel.getId(), userModel.getFirstName(), userModel.getLastName(), userModel.getEmail(), userModel.getPhone(), userModel.getPassword(), roles);
        }).orElseGet(() -> {
            logger.debug("User not found with name: {} and lastName: {}", firstName, lastName);
            return null;
        }));
    }


    public boolean deleteUser(long id) {
        logger.info("Starting to process Attempting to delete user with ID: {}", id);
        boolean result = userDao.deleteUser(id);
        if (result) {
            logger.info("User with ID {} deleted successfully", id);
        } else {
            logger.debug("User with ID {} not found or deletion failed", id);
        }
        return result;
    }

    public List<String> getRolesForEmail(String email) {
        logger.info("Starting to process Fetching roles for user with email: {}", email);
        Optional<UserModel> response = userDao.findUserByEmail(email);

        if (response.isPresent()) {
            List<String> roles = response.get().getRoles().stream()
                    .map(Role::getNameRole)
                    .collect(Collectors.toList());
            logger.info("Roles found for user {}: {}", email, roles);
            return roles;
        } else {
            logger.debug("User with email {} not found", email);
            throw new RuntimeException("User not found");
        }
    }

}
