package com.rafi.lmt.exception;

public class QueueHeldException extends RuntimeException {
    public QueueHeldException(String message) {
        super(message);
    }
}