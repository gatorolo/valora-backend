package com.valora.gestion.repository;

import com.valora.gestion.entity.Patient;
import com.valora.gestion.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    List<ServiceOrder> findByStatus(String status);
    List<ServiceOrder> findByStatusIn(List<String> statuses);

}
