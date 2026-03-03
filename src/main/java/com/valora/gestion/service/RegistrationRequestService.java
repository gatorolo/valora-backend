package com.valora.gestion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valora.gestion.entity.Caregiver;
import com.valora.gestion.entity.Notification;
import com.valora.gestion.entity.Patient;
import com.valora.gestion.entity.RegistrationRequest;
import com.valora.gestion.repository.CaregiverRepository;
import com.valora.gestion.repository.NotificationRepository;
import com.valora.gestion.repository.PatientRepository;
import com.valora.gestion.repository.RegistrationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationRequestService {

    @Autowired
    private RegistrationRequestRepository registrationRequestRepository;

    @Autowired
    private CaregiverRepository caregiverRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RegistrationRequest createRequest(RegistrationRequest request) {
        request.setStatus("PENDING");
        request.setCreatedAt(LocalDateTime.now());
        RegistrationRequest savedRequest = registrationRequestRepository.save(request);

        // Notify Admins
        Notification adminNotif = new Notification();
        adminNotif.setTitle("Nueva Solicitud de Registro");
        adminNotif.setMessage(
                "El usuario " + request.getApplicantName() + " solicita registrarse como " + request.getRole());
        adminNotif.setRecipientRole("admin");
        adminNotif.setType("registration");
        adminNotif.setDate(LocalDateTime.now());
        adminNotif.setRead(false);
        // We can use relatedPostId to store the registration ID so the admin UI knows
        // what to fetch
        adminNotif.setRelatedPostId(savedRequest.getId());

        notificationRepository.save(adminNotif);

        return savedRequest;
    }

    public List<RegistrationRequest> getPendingRequests() {
        return registrationRequestRepository.findByStatus("PENDING");
    }

    public RegistrationRequest approveRequest(Long id) throws Exception {
        Optional<RegistrationRequest> opt = registrationRequestRepository.findById(id);
        if (opt.isEmpty()) {
            throw new Exception("Registration request not found");
        }

        RegistrationRequest req = opt.get();
        if (!req.getStatus().equals("PENDING")) {
            throw new Exception("Request is not pending");
        }

        if (req.getRole().equalsIgnoreCase("CAREGIVER")) {
            Caregiver cg = objectMapper.readValue(req.getRawData(), Caregiver.class);
            cg.setStatus("Pendiente"); // Status inside the CG flow until fully verified
            caregiverRepository.save(cg);
        } else if (req.getRole().equalsIgnoreCase("PATIENT")) {
            Patient pt = objectMapper.readValue(req.getRawData(), Patient.class);
            pt.setStatus("Pendiente");
            patientRepository.save(pt);
        } else {
            throw new Exception("Unknown role: " + req.getRole());
        }

        req.setStatus("APPROVED");
        return registrationRequestRepository.save(req);
    }

    public RegistrationRequest rejectRequest(Long id) throws Exception {
        Optional<RegistrationRequest> opt = registrationRequestRepository.findById(id);
        if (opt.isEmpty()) {
            throw new Exception("Registration request not found");
        }

        RegistrationRequest req = opt.get();
        req.setStatus("REJECTED");
        return registrationRequestRepository.save(req);
    }
}
