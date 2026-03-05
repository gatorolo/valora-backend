package com.valora.gestion.repository;

import com.valora.gestion.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    // Historial del cuidador
    List<Shift> findByCaregiverIdOrderByEndTimeDesc(Long caregiverId);

    // Encontrar la guardia que está ACTIVA actualmente para un cuidador específico
    Optional<Shift> findTopByCaregiverIdAndStatusOrderByIdDesc(Long caregiverId, String status);

    // Encontrar todas las guardias activas para mostrar en el Dashboard
    List<Shift> findByStatus(String status);

    // Encontrar todas las guardias pendientes de pago para el Dashboard
    List<Shift> findByPaymentStatus(String paymentStatus);

    // Encontrar todas las guardias impagas por paciente
    List<Shift> findByPatientNameIgnoreCaseAndPatientPaymentStatus(String patientName, String patientPaymentStatus);

    // Encontrar TODAS las guardias impagas de pacientes (para tabla global de
    // Admin)
    List<Shift> findByPatientPaymentStatus(String patientPaymentStatus);

    List<Shift> findTop10ByPaymentStatusOrderByEndTimeDesc(String paymentStatus);
}
