package com.valora.gestion.service;

import com.valora.gestion.dto.*;
import com.valora.gestion.entity.User;
import com.valora.gestion.repository.UserRepository;
import com.valora.gestion.security.JwtService;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TotpService totpService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isTwoFactorEnabled()) {
            return AuthResponse.builder()
                    .requires2FA(true)
                    .username(user.getUsername())
                    .message("Verification code required")
                    .build();
        }

        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .role(user.getRole())
                .requires2FA(false)
                .message("Login successful")
                .build();
    }

    public TwoFactorSetupResponse setup2FA(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GoogleAuthenticatorKey key = totpService.generateSecret();
        user.setTwoFactorSecret(key.getKey());
        userRepository.save(user);

        String qrCodeUrl = totpService.getQrCodeUrl(user.getUsername(), key.getKey());

        return TwoFactorSetupResponse.builder()
                .qrCodeUrl(qrCodeUrl)
                .secret(key.getKey())
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
            throw new RuntimeException("Invalid verification code");
        }

        if (!user.isTwoFactorEnabled()) {
            user.setTwoFactorEnabled(true);
            userRepository.save(user);
        }

        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .role(user.getRole())
                .requires2FA(false)
                .message("Verification successful")
                .build();
    }
}
