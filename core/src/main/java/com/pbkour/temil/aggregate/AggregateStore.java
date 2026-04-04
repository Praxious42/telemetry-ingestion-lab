package com.pbkour.temil.aggregate;

import com.pbkour.temil.telemetry.TelemetryMessage;
import lombok.experimental.StandardException;

import java.util.HashMap;

public class AggregateStore {
    private final HashMap<MetricKey, Aggregate> map = new HashMap<>();


    void upsert(TelemetryMessage message) {
        if (message == null) throw new AggregateStoreException("message is null");

        MetricKey metricKey = new MetricKey(message);
        if (map.containsKey(metricKey)) {
            map.get(metricKey).update(message);
        } else {
            map.put(metricKey, new Aggregate(message));
        }
    }

    public Aggregate getAggregate(MetricKey metricKey) {
        if (metricKey == null) {
            throw new AggregateStoreException("Metric key is null");
        }

        return map.get(metricKey);
    }

    @StandardException
    public static class AggregateStoreException extends RuntimeException {
    }
}
