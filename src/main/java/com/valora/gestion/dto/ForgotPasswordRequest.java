package com.valora.gestion.dto;

import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {
    private String email;
}
