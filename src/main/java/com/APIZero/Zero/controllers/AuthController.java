package com.APIZero.Zero.controllers;

import com.APIZero.Zero.dto.LoginRequestDTO;
import com.APIZero.Zero.dto.TokenResponseDTO;
import com.APIZero.Zero.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto){
        TokenResponseDTO tokenResponse = authService.authenticate(dto);
        return ResponseEntity.ok(tokenResponse);
    }
}
