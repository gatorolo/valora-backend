package com.valora.gestion.repository;

import com.valora.gestion.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByCaregiverIdOrderByEndTimeDesc(Long caregiverId);
}
