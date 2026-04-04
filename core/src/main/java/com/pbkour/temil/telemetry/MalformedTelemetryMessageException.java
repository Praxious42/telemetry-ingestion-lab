package com.pbkour.temil.telemetry;

public class MalformedTelemetryMessageException extends RuntimeException {
    public MalformedTelemetryMessageException(String message) {
        super(message);
    }
}
