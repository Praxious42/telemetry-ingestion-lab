package com.pbkour.temil.aggregate;

import com.pbkour.temil.telemetry.TelemetryMessage;
import lombok.experimental.StandardException;

import java.util.HashMap;
import java.util.Optional;

public class AggregateStore {
    private final HashMap<MetricKey, Aggregate> map = new HashMap<>();


    public void upsert(TelemetryMessage message) {
        if (message == null) throw new AggregateStoreException("message is null");

        MetricKey metricKey = new MetricKey(message);
        Aggregate aggregate = map.get(metricKey);
        if (aggregate != null) {
            aggregate.update(message);
        } else {
            map.put(metricKey, new Aggregate(message));
        }
    }

    public Optional<Aggregate> getAggregate(MetricKey metricKey) {
        if (metricKey == null) {
            throw new AggregateStoreException("Metric key is null");
        }

        return Optional.ofNullable(map.get(metricKey));
    }

    public int getSize() {
        return map.size();
    }

    @StandardException
    public static class AggregateStoreException extends RuntimeException {
    }
}
