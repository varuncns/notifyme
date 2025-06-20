package com.notifyme.controller;

import com.notifyme.entity.NotificationLog;
import com.notifyme.service.NotificationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class NotificationDashboardController {

    private final NotificationLogService logService;

    @GetMapping("/summary")
    public Map<String, Object> getStats() {
        return logService.getSummaryStats();
    }

    @GetMapping("/logs")
    public Page<NotificationLog> getLogs(
            @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return logService.getFilteredLogs(type, status, page, size);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCSV(
            @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "") String status
    ) {
        byte[] data = logService.exportFilteredLogsToCSV(type, status);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=notification_logs.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(data);
    }
}
