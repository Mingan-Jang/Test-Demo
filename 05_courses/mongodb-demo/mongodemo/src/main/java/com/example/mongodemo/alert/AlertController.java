package com.example.mongodemo.alert;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody Alert alert) {
        return ResponseEntity.ok(alertService.save(alert));
    }

    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlerts() {
        return ResponseEntity.ok(alertService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alert> getAlertById(@PathVariable String id) {
        return alertService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable String id) {
        alertService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Alert>> getAlertsByEventId(@PathVariable String eventId) {
        return ResponseEntity.ok(alertService.findByEventId(eventId));
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<List<Alert>> getAlertsByLevel(@PathVariable String level) {
        return ResponseEntity.ok(alertService.findByLevel(level));
    }
}