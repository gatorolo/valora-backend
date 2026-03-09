package com.valora.gestion.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Service;

@Service
public class TotpService {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    /**
     * Generates a new TOTP secret key for a user.
     * 
     * @return GoogleAuthenticatorKey containing the secret and scratch codes.
     */
    public GoogleAuthenticatorKey generateSecret() {
        return gAuth.createCredentials();
    }

    /**
     * Validates a TOTP code against a secret key.
     * 
     * @param secret The user's secret key.
     * @param code   The 6-digit code to validate.
     * @return true if valid, false otherwise.
     */
    public boolean verifyCode(String secret, int code) {
        return gAuth.authorize(secret, code);
    }

    /**
     * Generates a QR code URL for the user to scan with their authenticator app.
     * 
     * @param label  The account name (e.g., username or email).
     * @param secret The user's secret key.
     * @return The QR code URL.
     */
    public String getQrCodeUrl(String label, String secret) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL("Valora", label,
                new GoogleAuthenticatorKey.Builder(secret).build());
    }
}
