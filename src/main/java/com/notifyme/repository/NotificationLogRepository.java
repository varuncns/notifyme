package com.notifyme.repository;

import com.notifyme.entity.NotificationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    Page<NotificationLog> findByTypeContainingAndStatusContaining(String type, String status, Pageable pageable);
}
