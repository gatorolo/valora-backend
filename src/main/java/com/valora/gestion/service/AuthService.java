package com.valora.gestion.service;

import com.valora.gestion.dto.*;
import com.valora.gestion.entity.User;
import com.valora.gestion.repository.UserRepository;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TotpService totpService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isTwoFactorEnabled(false)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .message("User registered successfully")
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (user.isTwoFactorEnabled()) {
            return AuthResponse.builder()
                    .requires2FA(true)
                    .username(user.getUsername())
                    .message("2FA required")
                    .build();
        }

        // Generate token (mock for now)
        String token = "mock-jwt-token-for-" + user.getUsername();

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .requires2FA(false)
                .build();
    }

    public TwoFactorSetupResponse setup2FA(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GoogleAuthenticatorKey key = totpService.generateSecret();
        user.setTwoFactorSecret(key.getSecret());
        userRepository.save(user);

        String qrCodeUrl = totpService.getQrCodeUrl(user.getUsername(), key.getSecret());

        return TwoFactorSetupResponse.builder()
                .qrCodeUrl(qrCodeUrl)
                .secret(key.getSecret())
                .build();
    }

    public AuthResponse verify2FA(TwoFactorVerifyRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getTwoFactorSecret() == null) {
            throw new RuntimeException("2FA not set up for this user");
        }

        boolean isValid = totpService.verifyCode(user.getTwoFactorSecret(), request.getCode());

        if (!isValid) {
            throw new RuntimeException("Invalid 2FA code");
        }

        // Enable 2FA if it was just setup
        if (!user.isTwoFactorEnabled()) {
            user.setTwoFactorEnabled(true);
            userRepository.save(user);
        }

        // Generate token (mock for now)
        String token = "mock-jwt-token-for-" + user.getUsername();

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .requires2FA(false)
                .message("2FA verification successful")
                .build();
    }
}
