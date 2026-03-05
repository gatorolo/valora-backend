package com.valora.gestion.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "caregivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Caregiver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("caregiverName")
    @Column(name = "caregiver_name")
    private String caregiverName;

    @JsonProperty("fullName")
    public void setFullName(String fullName) {
        if (this.caregiverName == null) {
            this.caregiverName = fullName;
        }
    }

    private String dni;
    private String phone;
    private String city;
    private Double hourlyRate;
    private String specialty;
    private String address;
    private String paymentTarget;
    private String status;
    @Column(columnDefinition = "TEXT")
    private String profilePhoto;
}
