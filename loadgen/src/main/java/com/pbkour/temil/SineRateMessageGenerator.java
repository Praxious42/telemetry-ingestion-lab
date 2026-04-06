package com.pbkour.temil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SineRateMessageGenerator {
    private final Random random = new Random();

    private final double baseRatePerMs;
    private final double amplitudePerMs;
    private final int periodMs;

    public SineRateMessageGenerator(double baseRatePerMs, double amplitudePerMs, int periodMs) {
        this.baseRatePerMs = baseRatePerMs;
        this.amplitudePerMs = amplitudePerMs;
        this.periodMs = periodMs;
    }

    public List<Long> generateMessageTimestamps(long startMillis, int durationMillis) {
        List<Long> timestamps = new ArrayList<>();

        for (int t = 0; t < durationMillis; t++) {
            double phase = 2.0 * Math.PI * (t % periodMs) / periodMs;
            double rate = baseRatePerMs + amplitudePerMs * (Math.sin(phase) + 1.0) / 2.0;

            int messageCountThisMs = samplePoisson(rate);

            for (int i = 0; i < messageCountThisMs; i++) {
                timestamps.add(startMillis + t);
            }
        }

        return timestamps;
    }

    // Knuth algorithm, fine for modest lambda
    private int samplePoisson(double lambda) {
        double l = Math.exp(-lambda);
        int k = 0;
        double p = 1.0;

        do {
            k++;
            p *= random.nextDouble();
        } while (p > l);

        return k - 1;
    }
}
