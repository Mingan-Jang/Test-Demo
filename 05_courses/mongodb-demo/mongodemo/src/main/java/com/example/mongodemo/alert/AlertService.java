package com.example.mongodemo.alert;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public Alert save(Alert alert) {
        return alertRepository.save(alert);
    }

    public List<Alert> findAll() {
        return alertRepository.findAll();
    }

    public Optional<Alert> findById(String id) {
        return alertRepository.findById(id);
    }

    public void deleteById(String id) {
        alertRepository.deleteById(id);
    }

    public List<Alert> findByEventId(String eventId) {
        return alertRepository.findByEventId(eventId);
    }

    public List<Alert> findByLevel(String level) {
        return alertRepository.findByLevel(level);
    }
}
