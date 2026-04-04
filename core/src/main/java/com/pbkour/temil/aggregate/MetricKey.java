package com.pbkour.temil.aggregate;

import com.pbkour.temil.telemetry.TelemetryMessage;

public record MetricKey(long deviceId, int metricId) {
    public MetricKey(TelemetryMessage message) {
        this(message.deviceId(), message.metricId());
    }
}
