package com.valora.gestion.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private boolean requires2FA;
    private String message;
}
