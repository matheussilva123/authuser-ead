package com.ead.authuser.service;

import com.ead.authuser.dto.UserDTO;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.exception.EmailAlreadyExistsException;
import com.ead.authuser.exception.UserNotFoundException;
import com.ead.authuser.exception.UsernameAlreadyExistsException;
import com.ead.authuser.model.User;
import com.ead.authuser.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(final UUID userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    @Override
    public void delete(final UUID userId) {
        final User user = findById(userId);
        userRepository.delete(user);
    }

    @Override
    public User save(final UserDTO userDTO) {
        if (checkUsernameExists(userDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("This username already exists.");
        }
        if (checkEmailExists(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException("This email already exists.");
        }
        var user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setUserStatus(UserStatus.ACTIVE);
        user.setUserType(UserType.STUDENT);
        user.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(final UserDTO userDTO, final UUID userId) {
        var user = findById(userId);
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setCpf(userDTO.getCpf());
        user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        return userRepository.save(user);
    }

    @Override
    public void updatePassword(final UserDTO userDTO, final UUID userId) {
        var user = findById(userId);
        if(user.getPassword().equals(userDTO.getOldPassword())) {
            user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
            user.setPassword(userDTO.getPassword());
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Error: Mismatched old password!");
        }
    }

    @Override
    public void updateOrCreateImage(final UserDTO userDTO, final UUID userId) {
        var user = findById(userId);
        user.setImageUrl(userDTO.getImageUrl());
        user.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        userRepository.save(user);
    }

    private boolean checkUsernameExists(final String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean checkEmailExists(final String email) {
        return userRepository.existsByEmail(email);
    }
}
