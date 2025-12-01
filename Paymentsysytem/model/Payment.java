package com.paymentsystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private String transactionId;
    private LocalDateTime timestamp;
    private String userId;
    private BigDecimal amount;
    private String method;
    public Payment() {}

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String safeToString() {
        // Return a safe serialization for storage/encryption. Do not include sensitive fields.
        return "transactionId=" + transactionId + ";userId=" + userId + ";amount=" + (amount != null ? amount.toPlainString() : "0");
    }
}

