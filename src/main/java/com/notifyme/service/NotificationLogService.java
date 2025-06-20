package com.notifyme.service;

import com.notifyme.entity.NotificationLog;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface NotificationLogService {
    Page<NotificationLog> getFilteredLogs(String type, String status, int page, int size);
    Map<String, Object> getSummaryStats();
    byte[] exportFilteredLogsToCSV(String type, String status);
}
