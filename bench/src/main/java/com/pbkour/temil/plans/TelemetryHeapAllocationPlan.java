package com.pbkour.temil.plans;

import com.pbkour.temil.telemetry.TelemetryEncoder;
import com.pbkour.temil.telemetry.TelemetryMessage;
import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
public class TelemetryHeapAllocationPlan {
    public byte[] bytes;

    @Setup(Level.Trial)
    public void setUp() {
        bytes = TelemetryEncoder.encode(new TelemetryMessage(10L, 10, 10L, 10.0));
    }
}
