package com.roninhub;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.roninhub.disruptor.*;

import java.util.concurrent.*;

public class DisruptorApp {

    public static void main(String[] args) throws InterruptedException {

        DisruptorHeartBeatMonitor monitor = new DisruptorHeartBeatMonitor(1000, 60); // Tick duration: 1 second

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
