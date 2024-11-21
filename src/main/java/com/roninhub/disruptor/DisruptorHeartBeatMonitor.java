package com.roninhub.disruptor;

import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisruptorHeartBeatMonitor {

    private final HashedWheelTimer timer;

    public DisruptorHeartBeatMonitor(long tickDuration, int wheelSize) {
        // Create a Disruptor for TimerEvent
        int bufferSize = 1024;
        TimerEventFactory factory = new TimerEventFactory();
        Disruptor<TimerEvent> disruptor = new Disruptor<>(
                factory,
                bufferSize,
                Executors.defaultThreadFactory()
        );

        // Create three consumers
        TimerEventHandler consumer1 = new TimerEventHandler("Main Processor");
        TimerEventHandler consumer2 = new TimerEventHandler("Log");
        TimerEventHandler consumer3 = new TimerEventHandler("Report");

        // Set up handlers (consumers)
        disruptor.handleEventsWith(consumer1, consumer2, consumer3);

        // Start the Disruptor
        disruptor.start();


        this.timer = new HashedWheelTimer(tickDuration, wheelSize, disruptor);
        this.timer.start();
    }

    public void onHeartbeat(String deviceId) {
        // Reset the timer for the device
        timer.addTask(deviceId, 30000); // 30 seconds timeout
    }

    public void stop() {
        timer.stop();
    }
}
