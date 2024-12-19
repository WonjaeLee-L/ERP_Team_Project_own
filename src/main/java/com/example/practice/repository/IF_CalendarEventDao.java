package com.example.practice.repository;

import com.example.practice.vo.CalendarEventDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IF_CalendarEventDao {
    List<CalendarEventDTO> getAllEvents(String erpId);
    CalendarEventDTO getEventById(Long id);
    void insertEvent(CalendarEventDTO event);
    void deleteEvent(Long id);
}
