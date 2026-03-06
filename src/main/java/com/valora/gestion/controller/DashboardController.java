package com.valora.gestion.controller;

import com.valora.gestion.entity.Shift;
import com.valora.gestion.repository.CaregiverRepository;
import com.valora.gestion.repository.PatientRepository;
import com.valora.gestion.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private CaregiverRepository caregiverRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        long totalCaregivers = caregiverRepository.count();
        long totalPatients = patientRepository.count();

        List<Shift> paidShifts = shiftRepository.findAll();
        double balance = paidShifts.stream()
                .filter(s -> "PAGADO".equals(s.getPatientPaymentStatus()))
                .mapToDouble(s -> s.getEarned() != null ? s.getEarned() : 0.0)
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCaregivers", totalCaregivers);
        stats.put("totalPatients", totalPatients);
        stats.put("totalBalance", balance);

        return ResponseEntity.ok(stats);
    }

    @PostMapping("/reset-balance")
    public ResponseEntity<?> resetBalance() {
        List<Shift> paidShifts = shiftRepository.findByPatientPaymentStatus("PAGADO");
        paidShifts.forEach(s -> {
            s.setPatientPaymentStatus("ARCHIVED_PAID");
        });
        shiftRepository.saveAll(paidShifts);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recent-payments")
    public ResponseEntity<?> getRecentPayments() {
        // Obtenemos las últimas 10 guardias que el administrador ya le pagó al cuidador
        List<Shift> recentShifts = shiftRepository.findTop10ByPaymentStatusOrderByEndTimeDesc("PAGADO");
        return ResponseEntity.ok(recentShifts);
    }
}
