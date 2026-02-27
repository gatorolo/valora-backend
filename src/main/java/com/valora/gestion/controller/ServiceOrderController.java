package com.valora.gestion.controller;

import com.valora.gestion.entity.ServiceOrder;
import com.valora.gestion.service.ServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-orders")
@CrossOrigin(origins = "http://localhost:4200")
public class ServiceOrderController {

    @Autowired
    private ServiceOrderService serviceOrderService;

    @PostMapping("/publish")
    public ResponseEntity<ServiceOrder> publish(@RequestBody ServiceOrder order) {
        // Usamos el método createOrder que ya tenías con la lógica del status y fecha
        ServiceOrder savedOrder = serviceOrderService.createOrder(order);
        return ResponseEntity.ok(savedOrder);
    }

    @PutMapping("/{id}/apply")
    public ResponseEntity<ServiceOrder> apply(
            @PathVariable Long id,
            @RequestParam Long caregiverId,
            @RequestParam String caregiverName) {

        // Llamamos al método que ya creamos en el Service
        ServiceOrder updatedOrder = serviceOrderService.applyToOrder(id, caregiverId, caregiverName);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/active")
    public List<ServiceOrder> getActiveOrders() {
        return serviceOrderService.getActiveOrders();
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<ServiceOrder> confirmOrder(
            @PathVariable Long id,
            @RequestParam Long caregiverId,
            @RequestParam String caregiverName){

        ServiceOrder confirmedOrder = serviceOrderService.confirmOrder(id, caregiverId, caregiverName);
        return ResponseEntity.ok(confirmedOrder);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        serviceOrderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
