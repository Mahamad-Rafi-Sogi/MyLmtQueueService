package com.rafi.lmt.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class LmtQueueElement {
    @Id
    @GeneratedValue
    private UUID id;

    private String lniata;

    private String data;

    @OneToOne
    @JoinColumn(name = "previous_id")
    private LmtQueueElement previous;

    @OneToOne
    @JoinColumn(name = "next_id")
    private LmtQueueElement next;


    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getLniata() { return lniata; }
    public void setLniata(String lniata) { this.lniata = lniata; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public LmtQueueElement getNext() { return next; }
    public void setNext(LmtQueueElement next) { this.next = next; }
    public LmtQueueElement getPrevious() { return previous; }
    public void setPrevious(LmtQueueElement previous) { this.previous = previous; }
}