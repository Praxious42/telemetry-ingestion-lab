package com.pbkour.temil.plans;

import com.pbkour.temil.TelemetryEncoder;
import com.pbkour.temil.TelemetryMessage;
import org.openjdk.jmh.annotations.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@State(Scope.Benchmark)
public class TelemetryHeapAllocationPlan {
    public byte[] bytes;

    @Setup(Level.Trial)
    public void setUp() {
        bytes = TelemetryEncoder.encode(new TelemetryMessage(10L, 10, 10L, 10.0));
    }
}
