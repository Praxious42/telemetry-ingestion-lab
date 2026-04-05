package com.pbkour.temil.plans;

import com.pbkour.temil.aggregate.AggregateStore;
import com.pbkour.temil.services.BatchIngestService;
import com.pbkour.temil.telemetry.TelemetryEncoder;
import com.pbkour.temil.telemetry.TelemetryMessage;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;

@State(Scope.Thread)
public class TelemetryAggregateUpsertPlan {
    @Param({"1000"})
    public int messageCount;

    @Param({"1", "10", "100", "1000"})
    public int distinctKeyCount;

    public AggregateStore aggregateStore;
    public BatchIngestService batchIngestService;
    public List<TelemetryMessage> messages;

    @Setup(Level.Invocation)
    public void setUp() {
        aggregateStore = new AggregateStore();
        batchIngestService = new BatchIngestService(aggregateStore);

    }

    @Setup(Level.Trial)
    public void setUpMessages() {
        messages = new ArrayList<>(messageCount);

        for (int i = 0; i < messageCount; i++) {
            long deviceId = (i % distinctKeyCount) + 1L;
            int metricId = 1;
            long timestamp = i + 1L;
            double value = i * 1.0;

            messages.add(new TelemetryMessage(deviceId, metricId, timestamp, value));
        }
    }
}
