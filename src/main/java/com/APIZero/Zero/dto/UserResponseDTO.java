package com.APIZero.Zero.dto;

import com.APIZero.Zero.entities.User;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String username,
        String email,
        LocalDateTime createdAt
) {
    //Metodo para auxiliar para converter a entidade do banco em DTO de saida
    public static UserResponseDTO fromEntity(User user){
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}