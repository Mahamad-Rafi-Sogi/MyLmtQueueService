package com.rafi.lmt.exception;

public class QueueStoppedException extends RuntimeException {
    public QueueStoppedException(String message) {
        super(message);
    }
}