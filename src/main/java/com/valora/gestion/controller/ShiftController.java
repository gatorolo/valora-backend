package com.valora.gestion.controller;

import com.valora.gestion.entity.Caregiver;
import com.valora.gestion.entity.Shift;
import com.valora.gestion.repository.CaregiverRepository;
import com.valora.gestion.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/shifts")
@CrossOrigin(origins = "http://localhost:4200")
public class ShiftController {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private CaregiverRepository caregiverRepository;

    @PostMapping("/start")
    public ResponseEntity<?> startShift(@RequestBody Map<String, Object> payload) {
        try {
            Long caregiverId = Long.valueOf(payload.get("caregiverId").toString());
            String patientName = payload.get("patientName") != null ? payload.get("patientName").toString()
                    : "Paciente Desconocido";

            Optional<Caregiver> caregiverOpt = caregiverRepository.findById(caregiverId);
            if (caregiverOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Caregiver not found");
            }

            // Verificar si ya tiene una guardia activa (opcional, por robustez)
            Optional<Shift> activeShiftOpt = shiftRepository.findTopByCaregiverIdAndStatusOrderByIdDesc(caregiverId,
                    "ACTIVA");
            if (activeShiftOpt.isPresent()) {
                return ResponseEntity.badRequest().body("El cuidador ya tiene una guardia activa corriendo.");
            }

            LocalDateTime startTime = LocalDateTime.now();

            Shift shift = new Shift(caregiverId, patientName, startTime, null, 0.0, 0.0, "ACTIVA", "PENDIENTE",
                    "PENDIENTE");
            Shift savedShift = shiftRepository.save(shift);

            return ResponseEntity.ok(savedShift);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error iniciando guardia: " + e.getMessage());
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<?> stopShift(@RequestBody Map<String, Object> payload) {
        try {
            Long caregiverId = Long.valueOf(payload.get("caregiverId").toString());
            Integer durationSeconds = Integer.valueOf(payload.get("durationSeconds").toString());

            Optional<Caregiver> caregiverOpt = caregiverRepository.findById(caregiverId);
            if (caregiverOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Caregiver not found");
            }

            // Buscamos la guardia que está ACTIVA
            Optional<Shift> activeShiftOpt = shiftRepository.findTopByCaregiverIdAndStatusOrderByIdDesc(caregiverId,
                    "ACTIVA");
            if (activeShiftOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("No hay una guardia activa para finalizar.");
            }

            Shift shift = activeShiftOpt.get();
            Caregiver caregiver = caregiverOpt.get();
            Double hourlyRate = caregiver.getHourlyRate() != null ? caregiver.getHourlyRate() : 0.0;

            Double durationHours = durationSeconds / 3600.0;
            Double earned = durationHours * hourlyRate;

            LocalDateTime endTime = LocalDateTime.now();

            // Actualizamos la guardia existente
            shift.setEndTime(endTime);
            shift.setDurationHours(durationHours);
            shift.setEarned(earned);
            shift.setStatus("FINALIZADA");

            Shift savedShift = shiftRepository.save(shift);

            return ResponseEntity.ok(savedShift);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error finalizando guardia: " + e.getMessage());
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Shift>> getActiveShifts() {
        List<Shift> shifts = shiftRepository.findByStatus("ACTIVA");
        return ResponseEntity.ok(shifts);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<Shift>> getUnpaidShifts() {
        List<Shift> shifts = shiftRepository.findByPaymentStatus("PENDIENTE");
        return ResponseEntity.ok(shifts);
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<?> payShift(@PathVariable Long id) {
        return shiftRepository.findById(id).map(shift -> {
            shift.setPaymentStatus("PAGADO");
            Shift updatedShift = shiftRepository.save(shift);
            return ResponseEntity.ok(updatedShift);
        }).orElse(ResponseEntity.notFound().build());
    }

    // --- NUEVOS ENDPOINTS DE DEUDA DE PACIENTE ---

    @GetMapping("/patient-unpaid")
    public ResponseEntity<List<Shift>> getAllPatientUnpaidShifts() {
        List<Shift> shifts = shiftRepository.findByPatientPaymentStatus("PENDIENTE");
        return ResponseEntity.ok(shifts);
    }

    @GetMapping("/patient/{patientName}/unpaid")
    public ResponseEntity<List<Shift>> getUnpaidShiftsByPatientName(@PathVariable String patientName) {
        List<Shift> shifts = shiftRepository.findByPatientNameAndPatientPaymentStatus(patientName, "PENDIENTE");
        return ResponseEntity.ok(shifts);
    }

    @PutMapping("/{id}/pay-patient")
    public ResponseEntity<?> payPatientShift(@PathVariable Long id) {
        return shiftRepository.findById(id).map(shift -> {
            shift.setPatientPaymentStatus("PAGADO");
            Shift updatedShift = shiftRepository.save(shift);
            return ResponseEntity.ok(updatedShift);
        }).orElse(ResponseEntity.notFound().build());
    }

    // ---------------------------------------------

    @GetMapping("/caregiver/{caregiverId}")
    public ResponseEntity<List<Shift>> getShiftsByCaregiver(@PathVariable Long caregiverId) {
        // Obtenemos solo las finalizadas para el historial
        List<Shift> shifts = shiftRepository.findByCaregiverIdOrderByEndTimeDesc(caregiverId)
                .stream()
                .filter(s -> "FINALIZADA".equals(s.getStatus()))
                .toList();

        return ResponseEntity.ok(shifts);
    }
}
