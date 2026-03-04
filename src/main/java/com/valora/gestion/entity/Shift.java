package com.valora.gestion.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "shifts")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long caregiverId;
    private String patientName;
    private Instant startTime;
    private Instant endTime;
    private Double durationHours;
    private Double earned;

    private String status;
    private String paymentStatus;
    private String patientPaymentStatus;

    public Shift() {
    }

    public Shift(Long caregiverId, String patientName, Instant startTime, Instant endTime,
            Double durationHours, Double earned, String status, String paymentStatus, String patientPaymentStatus) {
        this.caregiverId = caregiverId;
        this.patientName = patientName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationHours = durationHours;
        this.earned = earned;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.patientPaymentStatus = patientPaymentStatus;
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

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPatientPaymentStatus() {
        return patientPaymentStatus;
    }

    public void setPatientPaymentStatus(String patientPaymentStatus) {
        this.patientPaymentStatus = patientPaymentStatus;
    }
}
