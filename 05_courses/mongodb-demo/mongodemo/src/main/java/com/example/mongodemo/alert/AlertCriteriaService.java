package com.example.mongodemo.alert;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AlertCriteriaService {

    private final MongoTemplate mongoTemplate;

    public AlertCriteriaService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Alert> findByLevelAndEventId(String level, String eventId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("level").is(level)
                .and("eventId").is(eventId));
        return mongoTemplate.find(query, Alert.class);
    }

    public List<Alert> findByCategoriesContaining(String category) {
        Query query = new Query();
        query.addCriteria(Criteria.where("categories").in(category));
        return mongoTemplate.find(query, Alert.class);
    }

    public List<Alert> findByCreatedAfter(Date date) {
        Query query = new Query();
        query.addCriteria(Criteria.where("createdAt").gt(date));
        return mongoTemplate.find(query, Alert.class);
    }

    public List<Alert> findWithSortSkipLimit(Date startDate, String sortField, boolean asc, int skip, int limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("createdAt").gt(startDate));

        Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        query.with(Sort.by(direction, sortField));

        query.skip(skip);

        query.limit(limit);

        return mongoTemplate.find(query, Alert.class);
    }

    public Alert updateAlertMessageAndLevel(String id, String newMessage, String newLevel) {
        Query query = new Query(Criteria.where("id").is(id));

        Update update = new Update()
                .set("message", newMessage)
                .set("level", newLevel)
                .currentDate("updatedAt");

        return mongoTemplate.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                Alert.class);
    }

    /**
     * 根據 eventId 批量更新 level
     */
    public void updateLevelByEventId(String eventId, String newLevel) {

        Query query = new Query(Criteria.where("eventId").is(eventId));
        Update update = new Update().set("level", newLevel);

        UpdateResult result = mongoTemplate.updateMulti(query, update, Alert.class);

        log.info("更新 Alert level by eventId='{}', newLevel='{}', matchedCount={}, modifiedCount={}",
                eventId, newLevel, result.getMatchedCount(), result.getModifiedCount());
    }

    public void deleteCreatedBefore(Date date) {
        Query query = new Query(Criteria.where("createdAt").lt(date));
        DeleteResult result = mongoTemplate.remove(query, Alert.class);
        log.info("刪除 Alert createdAt < {}, deletedCount={}", date, result.getDeletedCount());
    }
}