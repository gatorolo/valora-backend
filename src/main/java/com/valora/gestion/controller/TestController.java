package com.valora.gestion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = { "http://localhost:4200", "https://valora-peach.vercel.app" }, allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS })
@RequestMapping("/api/test-cors")
public class TestController {

    @GetMapping
    public ResponseEntity<Map<String, String>> testGet() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "CORS GET diagnostic working! Backend is alive.");
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> testPost(@RequestBody Map<String, Object> payload) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "CORS POST diagnostic working! Data received: " + payload.toString());
        return ResponseEntity.ok(response);
    }
}
