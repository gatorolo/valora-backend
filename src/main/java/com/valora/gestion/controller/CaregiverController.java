package com.valora.gestion.controller;

import com.valora.gestion.entity.Caregiver;
import com.valora.gestion.repository.CaregiverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/caregivers")
public class CaregiverController {

    @Autowired
    private CaregiverRepository repository;

    @PostMapping
    public ResponseEntity<Caregiver> save(@RequestBody Caregiver caregiver) {
        // Al guardar, Java genera el ID automáticamente
        Caregiver saved = repository.save(caregiver);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Caregiver> findAll() {
        return repository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Caregiver> update(@PathVariable Long id, @RequestBody Caregiver caregiver) {
        caregiver.setId(id);
        return ResponseEntity.ok(repository.save(caregiver));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caregiver> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
