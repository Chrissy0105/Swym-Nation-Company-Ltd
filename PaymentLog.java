package com.swymnation.dms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_logs")
public class PaymentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;
    private Double amount;
    private String transactionId; // From the Gateway
    
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private LocalDateTime timestamp;

    public PaymentLog() {}

    public PaymentLog(Long clientId, Double amount, String transactionId, TransactionStatus status) {
        this.clientId = clientId;
        this.amount = amount;
        this.transactionId = transactionId;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public Long getClientId() { return clientId; }
    public Double getAmount() { return amount; }
    public String getTransactionId() { return transactionId; }
    public TransactionStatus getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
}