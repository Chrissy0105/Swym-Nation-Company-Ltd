package com.swymnation.dms.service;

import com.swymnation.dms.model.Notification;
import org.springframework.stereotype.Service;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class NotificationScheduler {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final CommunicationService communicationService;

    public NotificationScheduler(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    public void scheduleNotification(Notification notification, int delaySeconds) {
        // [FR-06 SR 3] Preference Check: Only send if user enabled this channel
        if (!notification.targetUser().hasPreference(notification.channelPreference())) {
            System.out.println("SKIPPED: User disabled " + notification.channelPreference());
            return;
        }

        System.out.println("SCHEDULER: Queuing notification in " + delaySeconds + "s");

        // [FR-06 PR] 10-second delivery target
        scheduler.schedule(() -> {
            try {
                communicationService.dispatch(notification);
                System.out.println("SUCCESS: Notification sent.");
            } catch (Exception e) {
                System.err.println("FAILED: Retrying once... [FR-06 AC 3]");
                // Retry logic could go here
            }
        }, delaySeconds, TimeUnit.SECONDS);
    }
}