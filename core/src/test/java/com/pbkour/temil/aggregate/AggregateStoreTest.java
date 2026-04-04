package com.pbkour.temil.aggregate;

import com.pbkour.temil.telemetry.TelemetryMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AggregateStoreTest {

    @Test
    void first_update_creates_aggregate() {
        AggregateStore aggregateStore = new AggregateStore();
        TelemetryMessage telemetryMessage = new TelemetryMessage(10L, 10, 10L, 10);

        aggregateStore.upsert(telemetryMessage);

        Aggregate aggregate = aggregateStore.getAggregate(new MetricKey(10L, 10)).orElseThrow(RuntimeException::new);

        assertEquals(telemetryMessage.value(), aggregate.getLastValue());
        assertEquals(telemetryMessage.value(), aggregate.getMin());
        assertEquals(telemetryMessage.value(), aggregate.getMax());
        assertEquals(telemetryMessage.value(), aggregate.getSum());
        assertEquals(1, aggregate.getCount());
        assertEquals(telemetryMessage.timestamp(), aggregate.getLastTimestamp());

    }

    @Test
    void repeated_updates_should_modify_metrics_correctly() {
        AggregateStore aggregateStore = new AggregateStore();
        TelemetryMessage telemetryMessage = new TelemetryMessage(10L, 10, 10L, 10);

        aggregateStore.upsert(telemetryMessage);

        TelemetryMessage telemetryMessage2 = new TelemetryMessage(10L, 10, 11L, 20);

        aggregateStore.upsert(telemetryMessage2);

        Aggregate aggregate = aggregateStore.getAggregate(new MetricKey(telemetryMessage)).orElseThrow(RuntimeException::new);

        assertEquals(telemetryMessage2.value(), aggregate.getLastValue());
        assertEquals(telemetryMessage.value(), aggregate.getMin());
        assertEquals(telemetryMessage2.value(), aggregate.getMax());
        assertEquals(telemetryMessage.value() + telemetryMessage2.value(), aggregate.getSum());
        assertEquals(2, aggregate.getCount());
        assertEquals(telemetryMessage2.timestamp(), aggregate.getLastTimestamp());
    }

    @Test
    void out_of_order_upserts_dont_override_old_values() {
        AggregateStore aggregateStore = new AggregateStore();
        TelemetryMessage telemetryMessage = new TelemetryMessage(10L, 10, 10L, 10);

        aggregateStore.upsert(telemetryMessage);

        TelemetryMessage telemetryMessage2 = new TelemetryMessage(10L, 10, 9L, 20);

        aggregateStore.upsert(telemetryMessage2);

        Aggregate aggregate = aggregateStore.getAggregate(new MetricKey(telemetryMessage)).orElseThrow(RuntimeException::new);

        assertEquals(telemetryMessage.value(), aggregate.getLastValue());
        assertEquals(telemetryMessage.value(), aggregate.getMin());
        assertEquals(telemetryMessage2.value(), aggregate.getMax());
        assertEquals(telemetryMessage.value() + telemetryMessage2.value(), aggregate.getSum());
        assertEquals(2, aggregate.getCount());
        assertEquals(telemetryMessage.timestamp(), aggregate.getLastTimestamp());
    }
}