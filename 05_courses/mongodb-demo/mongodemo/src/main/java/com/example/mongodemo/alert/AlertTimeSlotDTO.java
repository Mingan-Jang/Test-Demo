package com.example.mongodemo.alert;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class AlertTimeSlotDTO {
    private Instant timeSlot;
    private String level;
    private long count;
    private double avgCount;

}