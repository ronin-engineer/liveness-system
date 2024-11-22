package com.roninhub.wheel_timer;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TimingWheel {
    private final long tickDuration; // Tick duration in milliseconds
    private final int wheelSize;
    private final List<Set<TimerTask>> slots;
    private final AtomicInteger currentSlot = new AtomicInteger(0);
    private final ScheduledExecutorService executorService;
    private final Map<String, TimerTask> taskMap = new ConcurrentHashMap<>();

    public TimingWheel(long tickDuration, int wheelSize) {
        this.tickDuration = tickDuration;
        this.wheelSize = wheelSize;
        this.slots = new ArrayList<>(wheelSize);

        for (int i = 0; i < wheelSize; i++) {
            slots.add(Collections.newSetFromMap(new ConcurrentHashMap<>()));
        }

        this.executorService = Executors.newSingleThreadScheduledExecutor();
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

        slots.get(slotIndex).add(newTask);  // add to bucket O(1)
        taskMap.put(deviceId, newTask);     // put to map O(1)
    }

    private void advanceClock() {
        int slotIndex = currentSlot.getAndIncrement() % wheelSize;
        Set<TimerTask> bucket = slots.get(slotIndex);
        Iterator<TimerTask> iterator = bucket.iterator();

        while (iterator.hasNext()) { // O(M)
            TimerTask task = iterator.next();
            if (task.getRemainingRounds() > 0) {
                task.decreaseRounds();
            } else {
                // Timer expired
                iterator.remove(); // O(1)
                taskMap.remove(task.getDeviceId()); // O(1)
                // Handle the timeout (e.g., raise an alert)
                onTimeout(task.getDeviceId());
            }
        }
    }

    private void onTimeout(String deviceId) {
        System.out.println("Device timed out: " + deviceId);
        // Implement alert logic here
    }
}