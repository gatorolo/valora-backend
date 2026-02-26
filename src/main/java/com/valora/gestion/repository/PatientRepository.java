package com.valora.gestion.repository;

import com.valora.gestion.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient , Long> {

    Optional<Patient> findByNameIgnoreCase(String name);
}
