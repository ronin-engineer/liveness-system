package com.roninhub;


import com.roninhub.wheel_timer.HeartbeatMonitor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CustomLivenessApp
{
    public static void main(String[] args) {
        HeartbeatMonitor monitor = new HeartbeatMonitor(1000, 60); // Tick duration: 1 second

        monitor.onHeartbeat("device1");
        monitor.onHeartbeat("device2");

        // Schedule device1 to send heartbeats every 5 seconds
        ScheduledExecutorService device1Heartbeat = Executors.newSingleThreadScheduledExecutor();
        device1Heartbeat.scheduleAtFixedRate(() -> {
            monitor.onHeartbeat("device1");
            System.out.println("Heartbeat from device1");
        }, 5, 5, TimeUnit.SECONDS);

        // device2 stops sending heartbeats; it should time out after 30 seconds

        // Keep the main thread alive for simulation
        try {
            Thread.sleep(60000); // Run for 1 minute
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Shutdown resources
        device1Heartbeat.shutdown();
        monitor.stop();
    }
}
