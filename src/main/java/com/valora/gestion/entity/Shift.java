package com.valora.gestion.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shifts")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long caregiverId;
    private String patientName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double durationHours;
    private Double earned;

    public Shift() {
    }

    public Shift(Long caregiverId, String patientName, LocalDateTime startTime, LocalDateTime endTime,
            Double durationHours, Double earned) {
        this.caregiverId = caregiverId;
        this.patientName = patientName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationHours = durationHours;
        this.earned = earned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(Long caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Double getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Double durationHours) {
        this.durationHours = durationHours;
    }

    public Double getEarned() {
        return earned;
    }

    public void setEarned(Double earned) {
        this.earned = earned;
    }
}
