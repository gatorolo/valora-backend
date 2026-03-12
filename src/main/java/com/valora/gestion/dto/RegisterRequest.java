package com.valora.gestion.dto;

import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String role;
    private String email;
}
