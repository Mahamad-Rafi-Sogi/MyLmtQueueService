package com.rafi.lmt.dto;

import com.rafi.lmt.model.QueueState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class StateChangeRequest {

    @NotBlank(message = "lniata must not be empty")
    private String lniata;

    @NotNull(message = "state must not be empty")
    private QueueState state;

    public String getLniata() {
        return lniata;
    }

    public void setLniata(String lniata) {
        this.lniata = lniata;
    }

    public QueueState getState() {
        return state;
    }

    public void setState(QueueState state) {
        this.state = state;
    }
}