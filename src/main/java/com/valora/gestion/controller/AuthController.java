package com.valora.gestion.controller;

import com.valora.gestion.dto.*;
import com.valora.gestion.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow for development
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/setup-2fa")
    public ResponseEntity<TwoFactorSetupResponse> setup2FA(@RequestParam String username) {
        return ResponseEntity.ok(authService.setup2FA(username));
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<AuthResponse> verify2FA(@RequestBody TwoFactorVerifyRequest request) {
        return ResponseEntity.ok(authService.verify2FA(request));
    }
}
