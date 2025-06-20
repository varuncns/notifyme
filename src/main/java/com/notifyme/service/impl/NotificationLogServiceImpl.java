package com.notifyme.service.impl;

import com.notifyme.entity.NotificationLog;
import com.notifyme.repository.NotificationLogRepository;
import com.notifyme.service.NotificationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationLogServiceImpl implements NotificationLogService {

    private final NotificationLogRepository repository;

    @Override
    public Page<NotificationLog> getFilteredLogs(String type, String status, int page, int size) {
        return repository.findByTypeContainingAndStatusContaining(
                type == null ? "" : type,
                status == null ? "" : status,
                PageRequest.of(page, size)
        );
    }

    @Override
    public Map<String, Object> getSummaryStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", repository.count());
        stats.put("sent", repository.findByTypeContainingAndStatusContaining("", "SENT", PageRequest.of(0, 1)).getTotalElements());
        stats.put("failed", repository.findByTypeContainingAndStatusContaining("", "FAILED", PageRequest.of(0, 1)).getTotalElements());
        return stats;
    }

    @Override
    public byte[] exportFilteredLogsToCSV(String type, String status) {
        var logs = getFilteredLogs(type, status, 0, Integer.MAX_VALUE).getContent();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(out)) {
            writer.println("ID,Email,Phone,Subject,Message,Status,Type,Retry Count,Max Retry,Error,Timestamp");
            for (NotificationLog log : logs) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%d,%s,%s,%s\n",
                        log.getId(),
                        safe(log.getRecipientEmail()),
                        safe(log.getPhoneNumber()),
                        safe(log.getSubject()),
                        safe(log.getMessage()),
                        log.getStatus(),
                        log.getType(),
                        log.getRetryCount(),
                        log.getMaxRetryReached(),
                        safe(log.getError()),
                        log.getTimestamp()
                );
            }
            writer.flush();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV", e);
        }
    }

    private String safe(String s) {
        return s == null ? "" : s.replaceAll(",", " ").replaceAll("\n", " ");
    }
}
