package com.valora.gestion.controller;

import com.valora.gestion.entity.Caregiver;
import com.valora.gestion.entity.Shift;
import com.valora.gestion.repository.CaregiverRepository;
import com.valora.gestion.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private CaregiverRepository caregiverRepository;

    @GetMapping("/shifts-history")
    public ResponseEntity<?> getShiftsHistory() {
        // Obtenemos todas las guardias completadas (cobradas o pendientes).
        List<Shift> allShifts = shiftRepository.findAll();
        List<Caregiver> caregivers = caregiverRepository.findAll();

        Map<Long, String> caregiverMap = caregivers.stream()
                .collect(Collectors.toMap(
                        Caregiver::getId,
                        c -> c.getCaregiverName() != null ? c.getCaregiverName() : "Cuidador " + c.getId()));

        List<Map<String, Object>> reports = new ArrayList<>();

        for (Shift s : allShifts) {
            // Ignorar guardias "huérfanas" de un cuidador que fue eliminado
            if (s.getCaregiverId() == null || !caregiverMap.containsKey(s.getCaregiverId())) {
                continue;
            }

            Map<String, Object> row = new HashMap<>();
            row.put("id", s.getId());
            row.put("caregiverName", caregiverMap.getOrDefault(s.getCaregiverId(), "Cuidador Desconocido"));
            row.put("patientName", s.getPatientName() != null ? s.getPatientName() : "No asignado");
            row.put("startTime", s.getStartTime() != null ? s.getStartTime().toString() : null);
            row.put("endTime", s.getEndTime() != null ? s.getEndTime().toString() : null);
            row.put("status", s.getStatus());
            row.put("earned", s.getEarned() != null ? s.getEarned() : 0.0);
            row.put("paymentStatus", s.getPaymentStatus() != null ? s.getPaymentStatus() : "PENDIENTE");
            row.put("patientPaymentStatus",
                    s.getPatientPaymentStatus() != null ? s.getPatientPaymentStatus() : "PENDIENTE");

            // Calculo de horas
            if (s.getStartTime() != null && s.getEndTime() != null) {
                Duration duration = Duration.between(s.getStartTime(), s.getEndTime());
                long minutes = duration.toMinutes();
                double hours = minutes / 60.0;
                row.put("durationHours", Math.round(hours * 100.0) / 100.0); // 2 decimales
            } else {
                row.put("durationHours", 0.0);
            }

            reports.add(row);
        }

        // Ordenamos por fecha de finalizacion descendente
        reports.sort((a, b) -> {
            String endA = (String) a.get("endTime");
            String endB = (String) b.get("endTime");
            if (endA == null && endB == null)
                return 0;
            if (endA == null)
                return 1;
            if (endB == null)
                return -1;
            return endB.compareTo(endA);
        });

        return ResponseEntity.ok(reports);
    }
}
