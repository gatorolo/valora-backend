package com.valora.gestion.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("valora-noreply@valora.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String subject = "Recuperación de Contraseña - Valora";
        String resetUrl = "https://valora-peach.vercel.app/reset-password?token=" + token;
        
        String body = "Has solicitado restablecer tu contraseña en Valora.\n\n" +
                "Por favor, haz clic en el siguiente enlace para crear una nueva contraseña:\n" +
                resetUrl + "\n\n" +
                "Este enlace expirará en 1 hora.\n\n" +
                "Si no solicitaste este cambio, puedes ignorar este correo.";
        
        sendEmail(to, subject, body);
    }
}
