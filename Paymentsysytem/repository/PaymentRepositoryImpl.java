package com.paymentsystem.repository;

import com.paymentsystem.model.Payment;
import com.paymentsystem.model.Receipt;
import com.paymentsystem.security.EncryptionUtil;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    // simple in-memory store for receipts keyed by userId (for demo/testing)
    private final Map<String, List<Receipt>> receiptsStore = new ConcurrentHashMap<>();
    private final List<String> encryptedPayments = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void saveEncrypted(Payment payment) {
        String encrypted = EncryptionUtil.encrypt(payment.safeToString());
        encryptedPayments.add(encrypted);
        // NOTE: replace with real DB persistence (JPA/JDBC) in production
    }

    @Override
    public void saveReceipt(Receipt receipt) {
        receiptsStore.computeIfAbsent(receipt.getUserId(), k -> Collections.synchronizedList(new ArrayList<>())).add(receipt);
    }

    @Override
    public List<Receipt> getReceiptsByUser(String userId) {
        return new ArrayList<>(receiptsStore.getOrDefault(userId, new ArrayList<>()));
    }
}