package com.valora.gestion.controller;

import com.valora.gestion.entity.Caregiver;
import com.valora.gestion.entity.Patient;
import com.valora.gestion.entity.Shift;
import com.valora.gestion.repository.CaregiverRepository;
import com.valora.gestion.repository.PatientRepository;
import com.valora.gestion.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private CaregiverRepository caregiverRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @GetMapping("/settlements")
    public ResponseEntity<?> getSettlements() {
        List<Caregiver> caregivers = caregiverRepository.findAll();
        List<Shift> unpaidShifts = shiftRepository.findByPaymentStatus("PENDIENTE");

        List<Map<String, Object>> settlements = new ArrayList<>();

        for (Caregiver c : caregivers) {
            double totalOwed = unpaidShifts.stream()
                    .filter(s -> s.getCaregiverId() != null && s.getCaregiverId().equals(c.getId()))
                    .mapToDouble(s -> s.getEarned() != null ? s.getEarned() : 0.0)
                    .sum();

            if (totalOwed > 0) {
                Map<String, Object> settlement = new HashMap<>();
                settlement.put("id", c.getId());
                settlement.put("caregiverName", c.getCaregiverName());
                settlement.put("amount", totalOwed);
                settlement.put("status", "Pendiente");
                settlement.put("invoiceUploaded", false);
                settlements.add(settlement);
            }
        }

        return ResponseEntity.ok(settlements);
    }

    @GetMapping("/insurance-billing")
    public ResponseEntity<?> getInsuranceBilling() {
        List<Patient> patients = patientRepository.findAll();
        List<Shift> patientUnpaidShifts = shiftRepository.findByPatientPaymentStatus("PENDIENTE");

        List<Map<String, Object>> billingList = new ArrayList<>();

        for (Patient p : patients) {
            double patientDebt = patientUnpaidShifts.stream()
                    .filter(s -> s.getPatientName() != null && s.getPatientName().equalsIgnoreCase(p.getName()))
                    .mapToDouble(s -> s.getEarned() != null ? s.getEarned() : 0.0)
                    .sum();

            if (patientDebt > 0) {
                Map<String, Object> billing = new HashMap<>();
                billing.put("id", p.getId());
                billing.put("patientName", p.getName());
                billing.put("insuranceName",
                        p.getHealthInsurance() != null && !p.getHealthInsurance().isEmpty() ? p.getHealthInsurance()
                                : "Particular");
                billing.put("amount", patientDebt);
                billing.put("presentationDate", java.time.LocalDate.now().toString());

                int daysDel = (int) (patientDebt > 500000 ? 55 : (patientDebt > 200000 ? 30 : 5));

                billing.put("daysDelayed", daysDel);
                billing.put("status",
                        patientDebt > 500000 ? "Crítico" : (patientDebt > 200000 ? "Atrasado" : "Al día"));
                billingList.add(billing);
            }
        }

        return ResponseEntity.ok(billingList);
    }
}
