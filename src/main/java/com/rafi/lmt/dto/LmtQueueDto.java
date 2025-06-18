package com.rafi.lmt.dto;

import java.util.UUID;

public class LmtQueueDto {
    private Long lniata;
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

    public Long getLniata() {
        return lniata;
    }

    public void setLniata(Long lniata) {
        this.lniata = lniata;
    }
}