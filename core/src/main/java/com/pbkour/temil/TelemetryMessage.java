package com.pbkour.temil;

public record TelemetryMessage(long deviceId, int metricId, long timestamp, double value) {
}
