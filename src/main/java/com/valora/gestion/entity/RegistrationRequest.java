package com.valora.gestion.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "registration_requests")
@Data
@NoArgsConstructor
public class RegistrationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "CAREGIVER" or "PATIENT"
    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String applicantName;

    // "PENDING", "APPROVED", "REJECTED"
    @Column(nullable = false)
    private String status = "PENDING";

    // JSON payload storing the registration form data
    @Column(columnDefinition = "TEXT", nullable = false)
    private String rawData;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
