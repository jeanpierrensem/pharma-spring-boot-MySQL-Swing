package com.officine.losto.model;

import com.officine.losto.entity.AuthAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthAuditLogRepo extends JpaRepository<AuthAuditLog, Long> {
}
