package com.APIZero.Zero.services;

import com.APIZero.Zero.dto.LoginRequestDTO;
import com.APIZero.Zero.dto.TokenResponseDTO;
import com.APIZero.Zero.entities.User;
import com.APIZero.Zero.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenResponseDTO authenticate(LoginRequestDTO dto){
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new IllegalArgumentException("E-mail senha incorretos"));

        //Compara a senha enviada no DTO com o hash armazenado no banco
        if(!passwordEncoder.matches(dto.password(), user.getPassword())){
            throw new IllegalArgumentException("E-mail ou senha icorretos");
        }
        String token = jwtService.generateToken(user);
        return new TokenResponseDTO(token, "Bearer", jwtService.getExpirationTimeInSeconds());

    }

}
