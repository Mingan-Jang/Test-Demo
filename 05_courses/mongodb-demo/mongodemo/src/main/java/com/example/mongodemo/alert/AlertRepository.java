package com.example.mongodemo.alert;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AlertRepository extends MongoRepository<Alert, String> {

    List<Alert> findByEventId(String eventId);

    List<Alert> findByLevel(String level);

    @Query("{ 'tags': { $in: ?0 } }")
    List<Alert> findByTagsContaining(List<String> tags);
}