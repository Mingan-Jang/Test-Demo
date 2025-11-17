package com.example.mongodemo.alert;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlertService {
    private final MongoTemplate mongoTemplate;

    private final AlertRepository alertRepository;

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

    @PostConstruct
    public void init() {
        log.info("初始化 AlertService，執行 aggregateAlerts");
        aggregateAlerts(new Date(0), new Date());
        aggregateAlertsDTO();
    }

    public AggregationResults<AlertLevelCount> aggregateAlerts(Date startDate, Date endDate) {
        log.info("aggregateAlerts 開始執行，時間範圍: {} ~ {}", startDate, endDate);

        MatchOperation match = Aggregation.match(
                org.springframework.data.mongodb.core.query.Criteria.where("createdAt").gte(startDate).lte(endDate));

        GroupOperation group = Aggregation.group("level").count().as("count").first("level").as("level");

        SortOperation sort = Aggregation.sort(Sort.by(Sort.Direction.DESC, "count"));

        Aggregation aggregation = Aggregation.newAggregation(match, group, sort);

        AggregationResults<AlertLevelCount> results = mongoTemplate.aggregate(aggregation, "alerts",
                AlertLevelCount.class);

        log.info("aggregateAlerts 執行完成，結果筆數: {}", results.getMappedResults().size());
        results.getMappedResults().forEach(r -> log.info("Level: {}, Count: {}", r.getLevel(), r.getCount()));

        return results;
    }

    public List<AlertTimeSlotDTO> aggregateAlertsDTO() {
        Instant start = Instant.parse("2000-11-09T08:00:00Z");
        Instant end = Instant.parse("2025-11-09T09:00:00Z");

        // 1️⃣ match
        MatchOperation match = Aggregation.match(
                Criteria.where("createdAt").gte(start).lt(end));

        AddFieldsOperation addFields = Aggregation.addFields()
                .addFieldWithValue("timeSlot",
                        new Document("$toDate",
                                new Document("$subtract", Arrays.asList(
                                        new Document("$toLong", "$createdAt"),
                                        new Document("$mod", Arrays.asList(
                                                new Document("$toLong", "$createdAt"),
                                                1000 * 60 * 5))))))
                .build();

        GroupOperation group = Aggregation.group("timeSlot", "level")
                .count().as("count")
                .avg("1").as("avgCount"); // 改成其他欄位可計算平均值

        SortOperation sort = Aggregation.sort(Sort.by(Sort.Direction.ASC, "_id.timeSlot", "_id.level"));

        Aggregation aggregation = Aggregation.newAggregation(match, addFields, group, sort);

        List<Document> results = mongoTemplate.aggregate(aggregation, "alerts", Document.class).getMappedResults();

        return results.stream().map(doc -> {

            Document idDoc = (Document) doc.get("_id");
            Instant timeSlot = idDoc.getDate("timeSlot").toInstant();
            String level = idDoc.getString("level");

            Object countObj = doc.get("count");
            long count = (countObj instanceof Integer) ? ((Integer) countObj).longValue() : (Long) countObj;

            Object avgObj = doc.get("avgCount");
            double avgCount;
            if (avgObj instanceof Integer) {
                avgCount = ((Integer) avgObj).doubleValue();
            } else if (avgObj instanceof Double) {
                avgCount = (Double) avgObj;
            } else if (avgObj instanceof Long) {
                avgCount = ((Long) avgObj).doubleValue();
            } else {
                avgCount = 0.0;
            }

            AlertTimeSlotDTO dto = new AlertTimeSlotDTO(timeSlot, level, count, avgCount);
            log.info("Created DTO: {}", dto);
            return dto;
        }).collect(Collectors.toList());
    }

}
