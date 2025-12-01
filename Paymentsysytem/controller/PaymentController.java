package com.paymentsystem.controller;

import java.util.List; 
import com.paymentsystem.model.Receipt;
import com.paymentsystem.model.Payment;
import com.paymentsystem.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService service) {
        this.paymentService = service;
    }

    @PostMapping("/pay")
    public ResponseEntity<Receipt> makePayment(@RequestBody Payment payment) {
        Receipt receipt = paymentService.processPayment(payment);
        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/receipts/{userId}")
    public ResponseEntity<List<Receipt>> getReceipts(@PathVariable String userId) {
        return ResponseEntity.ok(paymentService.getReceipts(userId));
    }
}