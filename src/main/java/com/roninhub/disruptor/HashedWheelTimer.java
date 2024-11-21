package com.roninhub.disruptor;

import com.lmax.disruptor.dsl.Disruptor;
import com.roninhub.wheel_timer.TimerTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HashedWheelTimer {
    private final long tickDuration; // Tick duration in milliseconds
    private final int wheelSize;
    private final List<Set<TimerTask>> slots;
    private final AtomicInteger currentSlot = new AtomicInteger(0);
    private final ScheduledExecutorService executorService;
    private final Map<String, TimerTask> taskMap = new ConcurrentHashMap<>();
    private final Disruptor<TimerEvent> disruptor;

    public HashedWheelTimer(long tickDuration, int wheelSize, Disruptor<TimerEvent> disruptor) {
        this.tickDuration = tickDuration;
        this.wheelSize = wheelSize;
        this.slots = new ArrayList<>(wheelSize);

        for (int i = 0; i < wheelSize; i++) {
            slots.add(Collections.newSetFromMap(new ConcurrentHashMap<>()));
        }

        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.disruptor = disruptor;
    }

    // Start the timing wheel
    public void start() {
        executorService.scheduleAtFixedRate(this::advanceClock, tickDuration, tickDuration, TimeUnit.MILLISECONDS);
    }

    // Stop the timing wheel
    public void stop() {
        executorService.shutdown();
    }

    // Add or reset a timer task for a device
    public void addTask(String deviceId, long delay) {
        // Remove existing task if present
        TimerTask oldTask = taskMap.remove(deviceId);
        if (oldTask != null) {
            slots.get(oldTask.getSlotIndex()).remove(oldTask);
        }

        // Calculate the number of ticks and rounds
        long totalTicks = delay / tickDuration;
        int ticks = (int) (totalTicks % wheelSize);
        int rounds = (int) (totalTicks / wheelSize);

        int slotIndex = (currentSlot.get() + ticks) % wheelSize;

        TimerTask newTask = new TimerTask(deviceId, rounds, slotIndex);

        slots.get(slotIndex).add(newTask);
        taskMap.put(deviceId, newTask);
    }

    private void advanceClock() {
        int slotIndex = currentSlot.getAndIncrement() % wheelSize;
        Set<TimerTask> bucket = slots.get(slotIndex);
        Iterator<TimerTask> iterator = bucket.iterator();

        while (iterator.hasNext()) {
            TimerTask task = iterator.next();
            publishToDisruptor(task);
        }
    }

    private void onTimeout(String deviceId) {
        System.out.println("Device timed out: " + deviceId);
        // Implement alert logic here
    }

    private void publishToDisruptor(TimerTask task) {
        disruptor.publishEvent((event, sequence) -> event.setTimerTask(task));
    }
}
