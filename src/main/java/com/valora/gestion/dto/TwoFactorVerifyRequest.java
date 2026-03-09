package com.valora.gestion.dto;

import lombok.Data;

@Data
public class TwoFactorVerifyRequest {
    private String username;
    private int code;
}
