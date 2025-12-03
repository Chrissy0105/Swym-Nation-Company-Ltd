package com.swymnation.dms.service;

import com.swymnation.dms.model.Client;
import org.springframework.stereotype.Service;

public interface PaymentGatewayProxy {
    String initiatePayment(Client client);
}

// Temporary Mock Implementation so the app runs
@Service
class MockPaymentGateway implements PaymentGatewayProxy {
    @Override
    public String initiatePayment(Client client) {
        System.out.println("MOCK GATEWAY: Processing payment for " + client.getEmail());
        return "TXN-" + System.currentTimeMillis();
    }
}