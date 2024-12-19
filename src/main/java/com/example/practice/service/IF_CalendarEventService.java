package com.example.practice.service;

import com.example.practice.vo.CalendarEventDTO;

import java.util.List;

public interface IF_CalendarEventService {
    List<CalendarEventDTO> getAllEvents(String erpId);
    CalendarEventDTO getEventById(Long id);
    CalendarEventDTO createEvent(CalendarEventDTO event);
    void deleteEvent(Long id);
}