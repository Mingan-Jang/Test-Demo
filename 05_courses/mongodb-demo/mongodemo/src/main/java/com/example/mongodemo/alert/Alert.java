package com.example.mongodemo.alert;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "alerts")
public class Alert {

    @Id
    private String id;

    private String eventId;
    private String message;
    private Date createdAt;
    private String level;
    private Map<String, String> metadata;
    private List<String> tags;
    private byte[] attachment;

    // 需要 @Field 的情況,欄位名稱不一致
    private Set<String> categories; // 使用 Set<String> 保證唯一
}
