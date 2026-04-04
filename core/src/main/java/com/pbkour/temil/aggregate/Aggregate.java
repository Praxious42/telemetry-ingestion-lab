package com.pbkour.temil.aggregate;


import com.pbkour.temil.telemetry.TelemetryMessage;
import lombok.Getter;

@Getter
public class Aggregate {
    private long count;
    private double sum;
    private double min;
    private double max;
    private double lastValue;
    private long lastTimestamp;

    public Aggregate(long count, double sum, double min, double max, double lastValue, long lastTimestamp) {
        this.count = count;
        this.sum = sum;
        this.min = min;
        this.max = max;
        this.lastValue = lastValue;
        this.lastTimestamp = lastTimestamp;
    }

    public Aggregate(TelemetryMessage message) {
        this(1, message.value(), message.value(), message.value(), message.value(), message.timestamp());
    }

    public void update(TelemetryMessage message) {
        count++;
        sum += message.value();
        min = Math.min(min, message.value());
        max = Math.max(max, message.value());

        if (message.timestamp() > lastTimestamp) {
            lastTimestamp = message.timestamp();
            lastValue = message.value();
        }
    }
}
