package com.rafi.lmt.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

import java.io.Serializable;

public class EnqueueRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "lniata cannot be null")
    @Min(value = 100000, message = "lniata must be 6 digits")
    @Max(value = 999999, message = "lniata must be 6 digits")
    private Long lniata;

    @NotBlank(message = "data cannot be null")
    private String data;

    public EnqueueRequest() {}

    public EnqueueRequest(Long lniata, String data) {
        this.lniata = lniata;
        this.data = data;
    }

    public Long getLniata() {
        return lniata;
    }

    public void setLniata(Long lniata) {
        this.lniata = lniata;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}