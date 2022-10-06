package com.ead.authuser.controller;

import com.ead.authuser.dto.UserDTO;
import com.ead.authuser.model.User;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable(value = "userId") final UUID userId) {
        final User user = userService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "userId") final UUID userId) {
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") final UUID userId,
                                        @RequestBody @JsonView(UserDTO.UserView.UserPut.class) final UserDTO userDTO) {

        final var user = userService.updateUser(userDTO, userId);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<?> updatePassword(@PathVariable(value = "userId") final UUID userId,
                                            @RequestBody @JsonView(UserDTO.UserView.PasswordPut.class) final UserDTO userDTO) {
        userService.updatePassword(userDTO, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<?> updateImage(@PathVariable(value = "userId") final UUID userId,
                                         @RequestBody @JsonView(UserDTO.UserView.ImagePut.class) final UserDTO userDTO) {
        userService.updateOrCreateImage(userDTO, userId);
        return ResponseEntity.ok().build();
    }


}
