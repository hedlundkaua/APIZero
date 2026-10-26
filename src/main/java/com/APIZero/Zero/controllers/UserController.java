package com.APIZero.Zero.controllers;

import com.APIZero.Zero.dto.UserRequestDTO;
import com.APIZero.Zero.dto.UserResponseDTO;
import com.APIZero.Zero.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto){
        UserResponseDTO createUser = userService.createUsser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
