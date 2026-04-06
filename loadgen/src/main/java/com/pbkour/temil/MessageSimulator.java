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
                new LoadProfile(5000000, 1, true, TrafficPattern.SLOW),
                new LoadProfile(5000000, 10, true, TrafficPattern.SLOW),
                new LoadProfile(5000000, 100, true, TrafficPattern.SLOW),
                new LoadProfile(5000000, 1000, true, TrafficPattern.SLOW),
                new LoadProfile(5000000, 1, true, TrafficPattern.STEADY),
                new LoadProfile(5000000, 10, true, TrafficPattern.STEADY),
                new LoadProfile(5000000, 100, true, TrafficPattern.STEADY),
                new LoadProfile(5000000, 1000, true, TrafficPattern.STEADY),
                new LoadProfile(5000000, 1, true, TrafficPattern.BURSTY),
                new LoadProfile(5000000, 10, true, TrafficPattern.BURSTY),
                new LoadProfile(5000000, 100, true, TrafficPattern.BURSTY),
                new LoadProfile(5000000, 1000, true, TrafficPattern.BURSTY)
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
