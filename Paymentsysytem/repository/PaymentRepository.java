package com.paymentsystem.repository;

import java.util.List;
import com.paymentsystem.model.Payment;
import com.paymentsystem.model.Receipt;
public interface PaymentRepository {
    void saveEncrypted(Payment payment);
    void saveReceipt(Receipt receipt);
    List<Receipt> getReceiptsByUser(String userId);
}

