package com.pbkour.temil;

public class MalformedTelemetryMessageException extends RuntimeException {
    public MalformedTelemetryMessageException(String message) {
        super(message);
    }
}
