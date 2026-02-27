package com.valora.gestion.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "caregivers")
@Entity
public class Caregiver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caregiverName;
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

    public Caregiver() {
    }

    public Caregiver(Long id, String caregiverName, String dni, String phone, String city, String specialty,
            Double hourlyRate, String address, String paymentTarget, String status, String profilePhoto) {
        this.id = id;
        this.caregiverName = caregiverName;
        this.dni = dni;
        this.phone = phone;
        this.city = city;
        this.specialty = specialty;
        this.hourlyRate = hourlyRate;
        this.address = address;
        this.paymentTarget = paymentTarget;
        this.status = status;
        this.profilePhoto = profilePhoto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaregiverName() {
        return caregiverName;
    }

    public void setCaregiverName(String caregiverName) {
        this.caregiverName = caregiverName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentTarget() {
        return paymentTarget;
    }

    public void setPaymentTarget(String paymentTarget) {
        this.paymentTarget = paymentTarget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
