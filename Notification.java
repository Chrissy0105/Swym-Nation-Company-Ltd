package com.swymnation.dms.model;

public record Notification(
    String type,          // e.g., CLASS_REMINDER, PAYMENT_ALERT
    User targetUser,
    String subject,
    String body,
    String channelPreference // e.g., "EMAIL"
) {
    public Notification {
        // [FR-06 SR 4] Privacy Check: Ensure no sensitive payment details in body
        if (body.contains("credit card") || body.contains("CVV")) {
            throw new IllegalArgumentException("Security Violation: Notification contains sensitive payment data.");
        }
    }
}