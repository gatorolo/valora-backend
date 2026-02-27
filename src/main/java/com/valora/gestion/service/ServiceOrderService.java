package com.valora.gestion.service;

import com.valora.gestion.entity.ServiceOrder;
import com.valora.gestion.repository.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return repository.findByStatusIn(List.of("Publicado", "Postulado", "Confirmado"));
    }

    public ServiceOrder applyToOrder(Long orderId, Long caregiverId, String caregiverName) {
        return repository.findById(orderId).map(order -> {
            order.setStatus("Postulado");
            order.setCaregiverId(caregiverId);
            order.setCaregiverName(caregiverName);
            return repository.save(order);
        }).orElseThrow(() -> new RuntimeException("La guardia no existe"));
    }

    public ServiceOrder approveCaregiver(Long orderId) {
        ServiceOrder order = repository.findById(orderId).orElseThrow();

        order.setStatus("Confirmado");

        return repository.save(order);
    }

    public ServiceOrder confirmOrder(Long id, Long caregiverId, String caregiverName) {
        ServiceOrder order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        order.setStatus("Confirmado");
        order.setCaregiverId(caregiverId);
        order.setCaregiverName(caregiverName);
        return repository.save(order);
    }

    public void deleteOrder(Long id) {
        repository.deleteById(id);
    }
}
