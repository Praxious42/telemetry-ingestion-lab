package com.pbkour.temil;

import com.pbkour.temil.plans.TelemetryHeapAllocationPlan;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@Fork(value = 1)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class TelemetryBenchmark {
    @Benchmark
    public void decodeFromHeap(TelemetryHeapAllocationPlan telemetryHeapAllocationPlan) {
        for (int i = 0; i < telemetryHeapAllocationPlan.iterations; i++) {
            TelemetryDecoder.decode(telemetryHeapAllocationPlan.bytes);
        }
    }
}
