package com.roninhub.disruptor;

import com.lmax.disruptor.EventFactory;
import com.roninhub.wheel_timer.TimerTask;

public class TimerEventFactory implements EventFactory<TimerEvent> {

    @Override
    public TimerEvent newInstance() {
        return new TimerEvent();
    }
}
