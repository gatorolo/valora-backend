package com.valora.gestion.service;

import com.valora.gestion.entity.ServiceOrder;
import com.valora.gestion.repository.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services") // ASEGÚRATE de que en Angular la apiUrl sea esta misma
@CrossOrigin(origins = "http://localhost:4200")
public class ServiceController {

    @Autowired
    private ServiceOrderRepository repository;

    @PostMapping("/publish")
    public ResponseEntity<ServiceOrder> publishService(@RequestBody ServiceOrder order) {
        order.setStatus("Publicado"); // Aseguramos el estado inicial
        ServiceOrder savedOrder = repository.save(order);
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/active")
    public List<ServiceOrder> getActiveServices() {
        return repository.findByStatus("Publicado");
    }

    // ⭐ AGREGA ESTE MÉTODO PARA EL 404 ⭐
    @PutMapping("/{id}/apply")
    public ResponseEntity<ServiceOrder> applyToService(
            @PathVariable Long id,
            @RequestParam Long caregiverId) {

        return repository.findById(id).map(order -> {
            order.setCaregiverId(caregiverId);
            order.setStatus("Postulado"); // Cambiamos el estado
            ServiceOrder updated = repository.save(order);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }
}