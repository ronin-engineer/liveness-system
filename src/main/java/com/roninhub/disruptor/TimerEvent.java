package com.roninhub.disruptor;

import com.roninhub.wheel_timer.TimerTask;

public class TimerEvent {
    private TimerTask timerTask;

    public TimerEvent() {
        this.timerTask = timerTask;
    }

    public TimerEvent(TimerTask timerTask) {
        this.timerTask = timerTask;
    }

    public TimerTask getTimerTask() {
        return timerTask;
    }

    public void setTimerTask(TimerTask timerTask) {
        this.timerTask = timerTask;
    }
}
