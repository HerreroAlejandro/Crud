package com.api.crud.services;

import com.api.crud.DTO.UserDTO;
import com.api.crud.models.UserModel;
import com.api.crud.repositories.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DTOService {
        @Autowired
        private UserDao userDao;
    }
