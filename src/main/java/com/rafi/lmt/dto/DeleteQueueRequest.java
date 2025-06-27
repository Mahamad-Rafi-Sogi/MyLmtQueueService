package com.rafi.lmt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class DeleteQueueRequest {
    @NotBlank(message = "lniata cannot be blank")
    @Pattern(regexp = "^[0-9A-Fa-f]{6}$", message = "lniata must be 6 hex characters (0-9, A-F)")
    private String lniata;

    public String getLniata() {
        return lniata;
    }

    public void setLniata(String lniata) {
        this.lniata = lniata;
    }
}