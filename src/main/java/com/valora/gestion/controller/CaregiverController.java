package com.valora.gestion.controller;

import com.valora.gestion.entity.Caregiver;
import com.valora.gestion.repository.CaregiverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = { "http://localhost:4200", "https://valora-peach.vercel.app" }, allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
@RequestMapping("/api/caregivers")
public class CaregiverController {

    @Autowired
    private CaregiverRepository repository;

    @PostMapping
    public ResponseEntity<Caregiver> save(@RequestBody Caregiver caregiver) {
        System.out.println("DEBUG: Recibiendo cuidador: " + caregiver.getCaregiverName());
        Caregiver saved = repository.save(caregiver);
        System.out.println("DEBUG: Guardado cuidador con ID: " + saved.getId());
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
