package com.valora.gestion.service;

import com.valora.gestion.entity.ServiceOrder;
import com.valora.gestion.repository.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceOrderService {

    @Autowired
    private ServiceOrderRepository repository;

    public java.util.Optional<ServiceOrder> findById(Long id) {
        return repository.findById(id);
    }

    public ServiceOrder createOrder(ServiceOrder order) {
        order.setStatus("Publicado");
        return repository.save(order);
    }

    public ServiceOrder save(ServiceOrder order) {
        return repository.save(order);
    }

    public List<ServiceOrder> getActiveOrders() {
        return repository.findByStatus("Publicado");
    }

    public ServiceOrder applyToOrder(Long orderId, Long caregiverId, String caregiverName) {
        return repository.findById(orderId).map(order -> {
            // Al cambiarlo a "Postulado", el método getActiveOrders()
            // ya no lo encontrará porque él solo busca "Publicado"
            order.setStatus("Postulado");
            order.setCaregiverId(caregiverId);
            order.setCaregiverName(caregiverName);
            return repository.save(order);
        }).orElseThrow(() -> new RuntimeException("La guardia no existe"));
    }
}
