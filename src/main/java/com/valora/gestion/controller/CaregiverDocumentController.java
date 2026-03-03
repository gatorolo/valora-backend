package com.valora.gestion.controller;

import com.valora.gestion.entity.CaregiverDocument;
import com.valora.gestion.repository.CaregiverDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class CaregiverDocumentController {

    @Autowired
    private CaregiverDocumentRepository repository;

    @PostMapping("/upload")
    public ResponseEntity<CaregiverDocument> upload(@RequestBody CaregiverDocument document) {
        return ResponseEntity.ok(repository.save(document));
    }

    @GetMapping("/caregiver/{id}")
    public List<CaregiverDocument> getByCaregiver(@PathVariable Long id) {
        return repository.findByCaregiverId(id);
    }

    @GetMapping("/all")
    public List<CaregiverDocument> getAll() {
        return repository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
