package com.APIZero.Zero.dto;

public record TokenResponseDTO(
   String accessToken,
   String tokenType,
   Long expiresInSeconds
) {}
