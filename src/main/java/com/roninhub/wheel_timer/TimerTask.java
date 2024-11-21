package com.roninhub.wheel_timer;

import java.util.concurrent.atomic.AtomicInteger;

public class TimerTask {
    private String deviceId;
    private AtomicInteger remainingRounds;
    private int slotIndex;

    public TimerTask() {}

    public TimerTask(String deviceId, int remainingRounds, int slotIndex) {
        this.deviceId = deviceId;
        this.remainingRounds = new AtomicInteger(remainingRounds);
        this.slotIndex = slotIndex;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getRemainingRounds() {
        return remainingRounds.get();
    }

    public void decreaseRounds() {
        remainingRounds.decrementAndGet();
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(int slotIndex) {
        this.slotIndex = slotIndex;
    }
}