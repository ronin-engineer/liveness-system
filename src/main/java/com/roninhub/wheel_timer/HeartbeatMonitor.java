package com.roninhub.wheel_timer;

public class HeartbeatMonitor {
    private final TimingWheel timingWheel;

    public HeartbeatMonitor(long tickDuration, int wheelSize) {
        this.timingWheel = new TimingWheel(tickDuration, wheelSize);
        this.timingWheel.start();
    }

    // Call this method when a heartbeat is received
    public void onHeartbeat(String deviceId) {
        // Reset the timer for the device
        timingWheel.addTask(deviceId, 30000); // 30 seconds timeout
    }

    // Stop the monitor when shutting down
    public void stop() {
        timingWheel.stop();
    }
}