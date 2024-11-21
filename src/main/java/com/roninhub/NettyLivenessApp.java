package com.roninhub;

import com.roninhub.netty_timer.NettyHeartbeatMonitor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NettyLivenessApp {
    public static void main(String[] args) {
        NettyHeartbeatMonitor monitor = new NettyHeartbeatMonitor();

        // Simulate heartbeats
        monitor.onHeartbeat("device1");
        monitor.onHeartbeat("device2");

        // Simulate device1 sending heartbeats every 5 seconds
        ScheduledExecutorService device1Heartbeat = Executors.newSingleThreadScheduledExecutor();
        device1Heartbeat.scheduleAtFixedRate(() -> {
            monitor.onHeartbeat("device1");
            System.out.println("Heartbeat from device1");
        }, 5, 5, TimeUnit.SECONDS);

        // device2 does not send heartbeats and should time out

        // Keep the application running
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
