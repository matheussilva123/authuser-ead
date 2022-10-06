package com.ead.authuser.service;

import com.ead.authuser.dto.UserDTO;
import com.ead.authuser.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> findAll();

    User findById(UUID userId);

    void delete(UUID userId);

    User save(UserDTO userDTO);

    User updateUser(UserDTO userDTO, final UUID userId);

    void updatePassword(UserDTO userDTO, final UUID userId);

    void updateOrCreateImage(UserDTO userDTO, final UUID userId);
}
