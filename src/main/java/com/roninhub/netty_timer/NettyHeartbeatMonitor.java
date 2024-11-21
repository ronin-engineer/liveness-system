package com.roninhub.netty_timer;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class NettyHeartbeatMonitor {
    private final HashedWheelTimer timer;
    private final ConcurrentMap<String, Timeout> timeouts = new ConcurrentHashMap<>();

    public NettyHeartbeatMonitor() {
        // Create a timer with 1-second ticks and 60 slots
        this.timer = new HashedWheelTimer(1, TimeUnit.SECONDS, 60);
    }

    public void onHeartbeat(String deviceId) {
        // Cancel existing timeout if present
        Timeout oldTimeout = timeouts.remove(deviceId);
        if (oldTimeout != null) {
            oldTimeout.cancel();
        }

        // Schedule a new timeout
        Timeout newTimeout = timer.newTimeout(timeout -> {
            // Handle the timeout
            System.out.println("Device timed out: " + deviceId);
            timeouts.remove(deviceId);
        }, 30, TimeUnit.SECONDS);

        timeouts.put(deviceId, newTimeout);
    }

    // Stop the timer
    public void stop() {
        timer.stop();
    }
}
