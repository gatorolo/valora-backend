package com.valora.gestion.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TwoFactorSetupResponse {
    private String qrCodeUrl;
    private String secret;
}
