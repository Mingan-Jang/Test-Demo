package com.example.mongodemo.alert;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class AlertNosqlService {

    private final AlertRepository alertRepository;

    public AlertNosqlService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<Alert> findByTagsContaining(List<String> tags) {
        return alertRepository.findByTagsContaining(tags);
    }

}