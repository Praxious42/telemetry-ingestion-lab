package com.pbkour.temil.telemetry;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TelemetryEncoder {
    public static final int FRAME_SIZE = 32;

    private TelemetryEncoder() {
    }

    public static byte[] encode(TelemetryMessage message) {
        ByteBuffer buffer = ByteBuffer.allocate(FRAME_SIZE).order(ByteOrder.BIG_ENDIAN);

        buffer.putLong(message.deviceId());
        buffer.putInt(message.metricId());
        buffer.putLong(message.timestamp());
        buffer.putDouble(message.value());
        buffer.putInt(0);

        return buffer.array();
    }
}
