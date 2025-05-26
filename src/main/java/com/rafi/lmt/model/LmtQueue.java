package com.rafi.lmt.model;

import jakarta.persistence.*;

@Entity
public class LmtQueue {
    @Id
    private Long lniata;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "head_id")
    private LmtQueueElement head;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tail_id")
    private LmtQueueElement tail;

    private String printerGatewayUrl;

    @Enumerated(EnumType.STRING)
    private QueueState state;

    // Getters and Setters
    public Long getLniata() { return lniata; }
    public void setLniata(Long lniata) { this.lniata = lniata; }
    public LmtQueueElement getHead() { return head; }
    public void setHead(LmtQueueElement head) { this.head = head; }
    public LmtQueueElement getTail() { return tail; }
    public void setTail(LmtQueueElement tail) { this.tail = tail; }
    public String getPrinterGatewayUrl() { return printerGatewayUrl; }
    public void setPrinterGatewayUrl(String printerGatewayUrl) { this.printerGatewayUrl = printerGatewayUrl; }
    public QueueState getState() { return state; }
    public void setState(QueueState state) { this.state = state; }
}