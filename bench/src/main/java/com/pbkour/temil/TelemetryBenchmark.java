package com.pbkour.temil;

import com.pbkour.temil.plans.TelemetryHeapAllocationPlan;
import com.pbkour.temil.telemetry.TelemetryDecoder;
import org.openjdk.jmh.annotations.*;

public class TelemetryBenchmark {
    @Benchmark
    public void decodeFromHeap(TelemetryHeapAllocationPlan telemetryHeapAllocationPlan) {
        TelemetryDecoder.decode(telemetryHeapAllocationPlan.bytes);
    }
}
