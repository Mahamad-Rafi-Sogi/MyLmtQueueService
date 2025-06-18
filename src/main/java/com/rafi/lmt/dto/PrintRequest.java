package com.rafi.lmt.dto;

import java.io.Serializable;

public class PrintRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long lniata;
    private byte[] data;

    public PrintRequest() {}

    public PrintRequest(Long lniata, byte[] data) {
        this.lniata = lniata;
        this.data = data;
    }

    public Long getLniata() {
        return lniata;
    }

    public void setLniata(Long lniata) {
        this.lniata = lniata;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}