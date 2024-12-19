package com.example.practice.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CalendarEventDTO {
    private Long id;
    private String title;
    private String start;
    private String end;
    private Boolean allDay;
    private String erpId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
