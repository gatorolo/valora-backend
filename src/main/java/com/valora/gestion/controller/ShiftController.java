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

    @PostMapping("/stop")
    public ResponseEntity<?> stopShift(@RequestBody Map<String, Object> payload) {
        try {
            Long caregiverId = Long.valueOf(payload.get("caregiverId").toString());
            String patientName = payload.get("patientName") != null ? payload.get("patientName").toString()
                    : "Paciente Desconocido";
            Integer durationSeconds = Integer.valueOf(payload.get("durationSeconds").toString());

            Optional<Caregiver> caregiverOpt = caregiverRepository.findById(caregiverId);
            if (caregiverOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Caregiver not found");
            }

            Caregiver caregiver = caregiverOpt.get();
            Double hourlyRate = caregiver.getHourlyRate() != null ? caregiver.getHourlyRate() : 0.0;

            Double durationHours = durationSeconds / 3600.0;
            Double earned = durationHours * hourlyRate;

            LocalDateTime endTime = LocalDateTime.now();
            LocalDateTime startTime = endTime.minusSeconds(durationSeconds);

            Shift shift = new Shift(caregiverId, patientName, startTime, endTime, durationHours, earned);
            Shift savedShift = shiftRepository.save(shift);

            return ResponseEntity.ok(savedShift);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error finalizando guardia: " + e.getMessage());
        }
    }

    @GetMapping("/caregiver/{caregiverId}")
    public ResponseEntity<List<Shift>> getShiftsByCaregiver(@PathVariable Long caregiverId) {
        List<Shift> shifts = shiftRepository.findByCaregiverIdOrderByEndTimeDesc(caregiverId);
        return ResponseEntity.ok(shifts);
    }
}
