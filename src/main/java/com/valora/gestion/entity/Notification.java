package com.valora.gestion.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String message;
    private String type;
    private Instant date;

    @Column(name = "is_read")
    private boolean isRead;

    private Long relatedPostId;

    private String recipientRole;

    private String status;

    public Notification(String title, String message, String type, String recipientRole, String status,
            Long relatedPostId) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.recipientRole = recipientRole;
        this.status = status;
        this.relatedPostId = relatedPostId;
        this.date = Instant.now();
        this.isRead = false;
    }
}
