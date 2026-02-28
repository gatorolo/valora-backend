package com.valora.gestion.controller;

import com.valora.gestion.entity.Notification;
import com.valora.gestion.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAllByOrderByDateDesc();
    }

    @GetMapping("/role/{role}")
    public List<Notification> getNotificationsByRole(@PathVariable String role) {
        return notificationRepository.findByRecipientRoleOrderByDateDesc(role);
    }

    @PostMapping
    public Notification createNotification(@RequestBody Notification notification) {
        if (notification.getDate() == null) {
            notification.setDate(java.time.LocalDateTime.now());
        }
        return notificationRepository.save(notification);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        Optional<Notification> opt = notificationRepository.findById(id);
        if (opt.isPresent()) {
            Notification notif = opt.get();
            notif.setRead(true);
            notificationRepository.save(notif);
            return ResponseEntity.ok(notif);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/role/{role}")
    public ResponseEntity<?> clearByRole(@PathVariable String role) {
        List<Notification> list = notificationRepository.findByRecipientRoleOrderByDateDesc(role);
        notificationRepository.deleteAll(list);
        return ResponseEntity.ok().build();
    }
}
