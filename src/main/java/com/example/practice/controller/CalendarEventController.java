package com.example.practice.controller;

import com.example.practice.service.CalendarEventServiceImpl;
import com.example.practice.vo.CalendarEventDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CalendarEventController {

    private final CalendarEventServiceImpl eventService;

    @GetMapping("/eventAll")
    public List<CalendarEventDTO> getAllEvents(HttpSession session) {
        String erpId = (String) session.getAttribute("erpId");
        return eventService.getAllEvents(erpId);
    }

    @GetMapping("/eventOne")
    public CalendarEventDTO getEvent(@RequestParam Long id) {
        return eventService.getEventById(id);
    }

    @PostMapping("/eventAdd")
    public CalendarEventDTO createEvent(@RequestBody CalendarEventDTO event, HttpSession session) {
        String erpId = (String) session.getAttribute("erpId");
        event.setErpId(erpId);
        return eventService.createEvent(event);
    }

    @DeleteMapping("/eventDel")
    public void deleteEvent(@RequestParam Long id) {
        eventService.deleteEvent(id);
    }
}