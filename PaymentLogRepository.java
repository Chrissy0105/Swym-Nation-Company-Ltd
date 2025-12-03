package com.swymnation.dms.repository;

import com.swymnation.dms.model.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long> {
    List<PaymentLog> findByClientId(Long clientId);
}