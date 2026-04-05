package com.pbkour.temil.services;

import com.pbkour.temil.aggregate.AggregateStore;
import com.pbkour.temil.telemetry.TelemetryMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;

import java.util.List;

@RequiredArgsConstructor
public class BatchIngestService {
    private final AggregateStore aggregateStore;

    public void updateStoreWithMessages(final List<TelemetryMessage> telemetryMessages) {
        if (telemetryMessages == null) {
            throw new BatchIngestServiceException("been provided null telemetryMessages");
        }
        telemetryMessages.forEach(this::upsertToAggregateStore);
    }

    private void upsertToAggregateStore(TelemetryMessage message) {
        try {
            aggregateStore.upsert(message);
        } catch (AggregateStore.AggregateStoreException e) {
            System.out.println("Received exception from store: " + e.getMessage());
        }
    }

    @StandardException
    public static class BatchIngestServiceException extends RuntimeException {
    }
}
