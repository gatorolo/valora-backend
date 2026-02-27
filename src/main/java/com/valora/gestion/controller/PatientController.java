package com.valora.gestion.controller;

import com.valora.gestion.dto.PatientDTO;
import com.valora.gestion.entity.Patient;
import com.valora.gestion.entity.ServiceOrder;
import com.valora.gestion.repository.PatientRepository;
import com.valora.gestion.repository.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:4200") // Para que Angular no rebote por seguridad
public class PatientController {

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 1. CREAR (El que necesita tu método POST de Angular)
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        // El repositorio guarda y devuelve el paciente con el ID generado
        Patient savedPatient = patientRepository.save(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patientDetails) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setName(patientDetails.getName());
                    patient.setAge(patientDetails.getAge());
                    patient.setHealthInsurance(patientDetails.getHealthInsurance());
                    patient.setDiagnosis(patientDetails.getDiagnosis());
                    patient.setLocationLink(patientDetails.getLocationLink());
                    patient.setStatus(patientDetails.getStatus());
                    patient.setCity(patientDetails.getCity());
                    patient.setZone(patientDetails.getZone());

                    // Manejo de la lista @ElementCollection
                    if (patientDetails.getMedications() != null) {
                        patient.getMedications().clear(); // Borramos los anteriores
                        patient.getMedications().addAll(patientDetails.getMedications()); // Agregamos los nuevos
                    }

                    Patient updated = patientRepository.save(patient);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> dtos = patientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setAge(patient.getAge());
        dto.setHealthInsurance(patient.getHealthInsurance());
        dto.setDiagnosis(patient.getDiagnosis());
        dto.setLocationLink(patient.getLocationLink());
        dto.setStatus(patient.getStatus());
        dto.setCity(patient.getCity());
        dto.setZone(patient.getZone());
        dto.setProfilePhoto(patient.getProfilePhoto());
        dto.setMedications(patient.getMedications());

        // Buscamos si tiene un cuidador asignado en ServiceOrders
        serviceOrderRepository.findAll().stream()
                .filter(order -> "Confirmado".equals(order.getStatus()))
                .filter(order -> order.getPatientName() != null
                        && order.getPatientName().equalsIgnoreCase(patient.getName()))
                .findFirst()
                .ifPresent(order -> dto.setAssignedCaregiver(order.getCaregiverName()));

        return dto;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patientRepository.delete(patient);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
