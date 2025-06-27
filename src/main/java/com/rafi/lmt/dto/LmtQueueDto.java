package com.rafi.lmt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public class LmtQueueDto {

    @NotBlank(message = "lniata cannot be blank")
    @Pattern(regexp = "^[0-9A-Fa-f]{6}$", message = "lniata must be 6 hex characters (0-9, A-F)")
    private String lniata;

    private UUID headId;
    private UUID tailId;
    private String printerGatewayUrl;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPrinterGatewayUrl() {
        return printerGatewayUrl;
    }

    public void setPrinterGatewayUrl(String printerGatewayUrl) {
        this.printerGatewayUrl = printerGatewayUrl;
    }

    public UUID getTailId() {
        return tailId;
    }

    public void setTailId(UUID tailId) {
        this.tailId = tailId;
    }

    public UUID getHeadId() {
        return headId;
    }

    public void setHeadId(UUID headId) {
        this.headId = headId;
    }

    public String getLniata() {
        return lniata;
    }

    public void setLniata(String lniata) {
        this.lniata = lniata;
    }
}