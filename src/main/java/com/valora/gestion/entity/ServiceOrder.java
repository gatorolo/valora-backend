package com.valora.gestion.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "service_orders")
public class ServiceOrder {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String patientName;
        private Integer age;
        private String city;
        private String zone;
        private String schedule;
        private String complexity; // "Baja", "Media", "Alta"
        private String specialty; // "Enfermería", etc.
        private String status = "Pendiente";
        private Long caregiverId;
        private String caregiverName;

        public ServiceOrder() {
        }

        public ServiceOrder(Long id, String patientName, Integer age, String zone, String city, String schedule,
                        String specialty, String complexity, String status, Long caregiverId, String caregiverName) {
                this.id = id;
                this.patientName = patientName;
                this.age = age;
                this.zone = zone;
                this.city = city;
                this.schedule = schedule;
                this.specialty = specialty;
                this.complexity = complexity;
                this.status = status;
                this.caregiverId = caregiverId;
                this.caregiverName = caregiverName;

        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getPatientName() {
                return patientName;
        }

        public void setPatientName(String patientName) {
                this.patientName = patientName;
        }

        public Integer getAge() {
                return age;
        }

        public void setAge(Integer age) {
                this.age = age;
        }

        public String getZone() {
                return zone;
        }

        public void setZone(String zone) {
                this.zone = zone;
        }

        public String getCity() {
                return city;
        }

        public void setCity(String city) {
                this.city = city;
        }

        public String getSchedule() {
                return schedule;
        }

        public void setSchedule(String schedule) {
                this.schedule = schedule;
        }

        public String getComplexity() {
                return complexity;
        }

        public void setComplexity(String complexity) {
                this.complexity = complexity;
        }

        public String getSpecialty() {
                return specialty;
        }

        public void setSpecialty(String specialty) {
                this.specialty = specialty;
        }

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        public Long getCaregiverId() {
                return caregiverId;
        }

        public void setCaregiverId(Long caregiverId) {
                this.caregiverId = caregiverId;
        }

        public String getCaregiverName() {
                return caregiverName;
        }

        public void setCaregiverName(String caregiverName) {
                this.caregiverName = caregiverName;
        }
}