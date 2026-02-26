package com.valora.gestion.controller;

import com.valora.gestion.entity.Patient;
import com.valora.gestion.repository.PatientRepository;
import com.valora.gestion.repository.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:4200") // Para que Angular no rebote por seguridad
public class PatientController {

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
        return patientRepository.findById(id)
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
