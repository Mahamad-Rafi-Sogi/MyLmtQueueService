package com.rafi.lmt.dto;

import com.rafi.lmt.model.QueueState;

public class StateChangeRequest {
    private Long lniata;
    private QueueState state;

    public Long getLniata() {
        return lniata;
    }

    public void setLniata(Long lniata) {
        this.lniata = lniata;
    }

    public QueueState getState() {
        return state;
    }

    public void setState(QueueState state) {
        this.state = state;
    }
}