package com.swymnation.dms.service;

import com.swymnation.dms.model.Client;
import com.swymnation.dms.model.PaymentLog;
import com.swymnation.dms.model.TransactionStatus;
import com.swymnation.dms.repository.PaymentLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentGatewayProxy gatewayProxy;
    private final PaymentLogRepository paymentLogRepository;

    @Autowired
    public PaymentService(PaymentGatewayProxy gatewayProxy, PaymentLogRepository repo) {
        this.gatewayProxy = gatewayProxy;
        this.paymentLogRepository = repo;
    }

    @Transactional
    public String processPayment(Client client, Double amount) {
        // 1. Call External Gateway [FR-03 Sys Req 1]
        String txnId = gatewayProxy.initiatePayment(client);
        
        // 2. Log the Transaction [FR-03 Sys Req 2]
        PaymentLog log = new PaymentLog(client.getId(), amount, txnId, TransactionStatus.SUCCESS);
        paymentLogRepository.save(log);

        // 3. Generate Receipt (Simulated) [FR-03 Sys Req 4]
        return generateReceipt(log);
    }

    private String generateReceipt(PaymentLog log) {
        // Simple string receipt to satisfy AC 4 (10 second limit)
        return "RECEIPT #" + log.getId() + " | Amount: $" + log.getAmount() + " | Status: " + log.getStatus();
    }
}