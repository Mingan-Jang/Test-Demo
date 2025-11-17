package com.example.mongodemo.alert;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
public class AlertLevelCount {
    private String level;
    private int count;

}