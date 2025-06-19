package com.rafi.lmt.dto;

import jakarta.validation.constraints.*;
import org.springframework.lang.NonNull;

import java.io.Serializable;

public class EnqueueRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "lniata cannot be blank")
    @Pattern(regexp = "^[0-9A-Fa-f]{6}$", message = "lniata must be 6 hex characters (0-9, A-F)")
    private String lniata;

    @NotBlank(message = "data cannot be null")
    private String data;

    public EnqueueRequest() {}

    public EnqueueRequest(String lniata, String data) {
        this.lniata = lniata;
        this.data = data;
    }

    public String getLniata() {
        return lniata;
    }

    public void setLniata(String lniata) {
        this.lniata = lniata;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}