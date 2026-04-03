package com.pbkour.temil.plans;

import org.openjdk.jmh.annotations.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@State(Scope.Benchmark)
public class TelemetryHeapAllocationPlan {
    @Param({ "100", "200", "300", "500", "1000" })
    public int iterations;

    public byte[] bytes;

    @Setup(Level.Invocation)
    public void setUp() {
        ByteBuffer buffer = ByteBuffer.allocate(32).order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(10L);
        buffer.putInt(10);
        buffer.putLong(10L);
        buffer.putDouble(10);

        bytes = buffer.array();
    }
}
