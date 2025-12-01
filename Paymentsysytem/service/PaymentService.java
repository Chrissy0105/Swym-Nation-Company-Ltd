package com.paymentsystem.service;

import com.paymentsystem.model.Payment;
import com.paymentsystem.model.Receipt;
import com.paymentsystem.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final NotificationService notificationService;

    public PaymentService(PaymentRepository repo, NotificationService notificationService) {
        this.paymentRepo = repo;
        this.notificationService = notificationService;
    }

    public Receipt processPayment(Payment payment) {
        paymentRepo.saveEncrypted(payment);
        Receipt receipt = generateReceipt(payment);
        paymentRepo.saveReceipt(receipt);
        sendReceipt(receipt, payment.getUserId());
        return receipt;
    }

    private Receipt generateReceipt(Payment payment) {
        Receipt r = new Receipt();
        r.setReceiptId(UUID.randomUUID().toString());
        r.setTransactionId(payment.getTransactionId());
        r.setUserId(payment.getUserId());
        r.setIssuedAt(LocalDateTime.now());
        r.setStatus("SUCCESS");
        return r;
    }

    private void sendReceipt(Receipt receipt, String userId) {
        notificationService.sendReceipt(receipt, userId);
    }
}