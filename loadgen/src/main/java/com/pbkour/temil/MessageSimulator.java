package com.pbkour.temil;

import com.pbkour.temil.aggregate.AggregateStore;
import com.pbkour.temil.services.BatchIngestService;
import com.pbkour.temil.telemetry.TelemetryMessage;
import org.HdrHistogram.Histogram;

import java.util.List;

public class MessageSimulator {

    public static void main(String[] args) {
        List<LoadProfile> loadProfiles = createLoadProfiles();
        loadProfiles.forEach(loadProfile -> {
            System.out.println(loadProfile);
            Histogram histogram = new Histogram(1, 60_000_000L, 3);
            List<TelemetryMessage> telemetryMessages = MessageGenerator.generateMessages(loadProfile);

            for (int i = 0; i < 50; i++) {

                BatchIngestService batchIngestService = new BatchIngestService(new AggregateStore());
                long start = System.nanoTime();

                batchIngestService.updateStoreWithMessages(telemetryMessages);

                long end = System.nanoTime();
                long durationMicros = (end - start) / 1_000;
                histogram.recordValue(durationMicros);
            }

            extractHistogramValues(histogram);
        });
    }

    private static List<LoadProfile> createLoadProfiles() {
        return List.of(
                new LoadProfile(5000000, 1, TrafficPattern.SLOW),
                new LoadProfile(5000000, 10, TrafficPattern.SLOW),
                new LoadProfile(5000000, 100, TrafficPattern.SLOW),
                new LoadProfile(5000000, 1000, TrafficPattern.SLOW),
                new LoadProfile(5000000, 1, TrafficPattern.STEADY),
                new LoadProfile(5000000, 10, TrafficPattern.STEADY),
                new LoadProfile(5000000, 100, TrafficPattern.STEADY),
                new LoadProfile(5000000, 1000, TrafficPattern.STEADY),
                new LoadProfile(5000000, 1, TrafficPattern.BURSTY),
                new LoadProfile(5000000, 10, TrafficPattern.BURSTY),
                new LoadProfile(5000000, 100, TrafficPattern.BURSTY),
                new LoadProfile(5000000, 1000, TrafficPattern.BURSTY)
        );
    }

    private static void extractHistogramValues(Histogram histogram) {
        System.out.println("count = " + histogram.getTotalCount());
        System.out.println("p50   = " + histogram.getValueAtPercentile(50.0) + " us");
        System.out.println("p95   = " + histogram.getValueAtPercentile(95.0) + " us");
        System.out.println("p99   = " + histogram.getValueAtPercentile(99.0) + " us");
        System.out.println("max   = " + histogram.getMaxValue() + " us");
        System.out.println("mean  = " + histogram.getMean() + " us");
    }
}
