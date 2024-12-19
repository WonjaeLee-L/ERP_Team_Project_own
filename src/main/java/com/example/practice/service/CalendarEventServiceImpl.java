package com.example.practice.service;

import com.example.practice.repository.IF_CalendarEventDao;
import com.example.practice.vo.CalendarEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalendarEventServiceImpl implements IF_CalendarEventService {

    private final IF_CalendarEventDao eventMapper;

    @Override
    public List<CalendarEventDTO> getAllEvents(String erpId) {
        return eventMapper.getAllEvents(erpId);
    }

    @Override
    public CalendarEventDTO getEventById(Long id) {
        return eventMapper.getEventById(id);
    }

    @Override
    public CalendarEventDTO createEvent(CalendarEventDTO event) {
        eventMapper.insertEvent(event);
        return event;
    }

    @Override
    public void deleteEvent(Long id) {
        eventMapper.deleteEvent(id);
    }
}