package com.valora.gestion.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "caregiver_documents")
@Data
@NoArgsConstructor
public class CaregiverDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long caregiverId;

    @Column(nullable = false)
    private String caregiverName;

    @Column(nullable = false)
    private String type; // e.g., DNI_FRONT, DNI_BACK, INSURANCE

    @Column(nullable = false)
    private String fileName;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content; // Base64 content

    @Column(nullable = false)
    private LocalDateTime uploadDate = LocalDateTime.now();

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, VERIFIED, REJECTED

    public CaregiverDocument(Long caregiverId, String caregiverName, String type, String fileName, String content) {
        this.caregiverId = caregiverId;
        this.caregiverName = caregiverName;
        this.type = type;
        this.fileName = fileName;
        this.content = content;
    }
}
