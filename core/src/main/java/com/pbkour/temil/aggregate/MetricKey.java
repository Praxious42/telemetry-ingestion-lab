package com.pbkour.temil.aggregate;

import com.pbkour.temil.telemetry.TelemetryMessage;

import java.util.Objects;

public record MetricKey(long deviceId, int metricId) {
    public MetricKey(TelemetryMessage message) {
        this(message.deviceId(), message.metricId());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MetricKey metricKey = (MetricKey) o;
        return metricId == metricKey.metricId && deviceId == metricKey.deviceId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, metricId);
    }
}
