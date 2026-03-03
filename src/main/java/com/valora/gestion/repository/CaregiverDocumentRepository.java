package com.valora.gestion.repository;

import com.valora.gestion.entity.CaregiverDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CaregiverDocumentRepository extends JpaRepository<CaregiverDocument, Long> {
    List<CaregiverDocument> findByCaregiverId(Long caregiverId);
}
