package com.paymentsystem.service;

import com.paymentsystem.model.Receipt;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendReceipt(Receipt receipt, String userId) {
        // Placeholder: in real app, send email/push notification.
        System.out.println("[NotificationService] Sending receipt " + receipt.getReceiptId() + " to user " + userId);
    }
}
