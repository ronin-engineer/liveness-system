package com.roninhub.disruptor;


import com.lmax.disruptor.EventHandler;
import com.roninhub.wheel_timer.TimerTask;

public class TimerEventHandler implements EventHandler<TimerEvent> {
    private final String consumerName;

    public TimerEventHandler(String consumerName) {
        this.consumerName = consumerName;
    }

    @Override
    public void onEvent(TimerEvent event, long sequence, boolean endOfBatch) throws Exception {
        TimerTask task = event.getTimerTask();
        System.out.println("Consumer " + consumerName + " processing task, device_id: " + task.getDeviceId() + " at " + System.currentTimeMillis());
    }
}
