package com.pbkour.temil.telemetry;

public record TelemetryMessage(long deviceId, int metricId, long timestamp, double value) {
}
