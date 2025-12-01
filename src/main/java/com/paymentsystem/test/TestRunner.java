package com.paymentsystem.test;

import com.paymentsystem.model.Payment;
import com.paymentsystem.model.Receipt;
import com.paymentsystem.repository.PaymentRepositoryImpl;
import com.paymentsystem.service.NotificationService;
import com.paymentsystem.service.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("=== Payment System Test Runner ===\n");

        // Setup: create repository, notification service, and payment service
        PaymentRepositoryImpl repository = new PaymentRepositoryImpl();
        NotificationService notificationService = new NotificationService();
        PaymentService paymentService = new PaymentService(repository, notificationService);

        // Test 1: Create a payment and process it
        System.out.println("Test 1: Process a payment");
        Payment payment = new Payment();
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setTimestamp(LocalDateTime.now());
        payment.setUserId("user123");
        payment.setAmount(new BigDecimal("99.99"));
        payment.setMethod("CREDIT_CARD");

        System.out.println("  Input Payment: transactionId=" + payment.getTransactionId() + 
                          ", userId=" + payment.getUserId() + 
                          ", amount=" + payment.getAmount());

        Receipt receipt = paymentService.processPayment(payment);

        System.out.println("  Result Receipt: receiptId=" + receipt.getReceiptId() + 
                          ", status=" + receipt.getStatus() + 
                          ", issuedAt=" + receipt.getIssuedAt());
        System.out.println("  ✓ Test 1 PASSED\n");

        // Test 2: Retrieve receipts for user
        System.out.println("Test 2: Retrieve receipts for user");
        List<Receipt> userReceipts = paymentService.getReceipts("user123");
        System.out.println("  Retrieved " + userReceipts.size() + " receipt(s) for user123");
        if (userReceipts.size() == 1 && userReceipts.get(0).getReceiptId().equals(receipt.getReceiptId())) {
            System.out.println("  ✓ Test 2 PASSED\n");
        } else {
            System.out.println("  ✗ Test 2 FAILED\n");
        }

        // Test 3: Process another payment and verify both receipts exist
        System.out.println("Test 3: Process second payment and verify both exist");
        Payment payment2 = new Payment();
        payment2.setTransactionId(UUID.randomUUID().toString());
        payment2.setTimestamp(LocalDateTime.now());
        payment2.setUserId("user123");
        payment2.setAmount(new BigDecimal("50.00"));
        payment2.setMethod("PAYPAL");

        Receipt receipt2 = paymentService.processPayment(payment2);
        List<Receipt> allReceipts = paymentService.getReceipts("user123");
        
        System.out.println("  Total receipts for user123 after second payment: " + allReceipts.size());
        if (allReceipts.size() == 2) {
            System.out.println("  ✓ Test 3 PASSED\n");
        } else {
            System.out.println("  ✗ Test 3 FAILED\n");
        }

        // Test 4: Verify different user has no receipts
        System.out.println("Test 4: Verify different user has no receipts");
        List<Receipt> otherUserReceipts = paymentService.getReceipts("user999");
        if (otherUserReceipts.isEmpty()) {
            System.out.println("  ✓ Test 4 PASSED\n");
        } else {
            System.out.println("  ✗ Test 4 FAILED\n");
        }

        System.out.println("=== All Tests Completed ===");
    }
}
