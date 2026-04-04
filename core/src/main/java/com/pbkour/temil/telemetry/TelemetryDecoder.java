package com.pbkour.temil.telemetry;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TelemetryDecoder {
    public static final int FRAME_SIZE = 32;

    private TelemetryDecoder() {
    }

    public static TelemetryMessage decode(byte[] bytes) {
        if (bytes.length != FRAME_SIZE) {
            throw new IllegalArgumentException(
                    "Expected " + FRAME_SIZE + " bytes but got " + bytes.length
            );
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes)
                .order(ByteOrder.BIG_ENDIAN);

        long deviceId = buffer.getLong();
        if (deviceId <= 0L) {
            throw new MalformedTelemetryMessageException("Device ID must be > 0");
        }

        int metricId = buffer.getInt();
        if (metricId <= 0) {
            throw new MalformedTelemetryMessageException("Metric ID must be > 0");
        }

        long timestamp = buffer.getLong();
        if (timestamp <= 0L) {
            throw new MalformedTelemetryMessageException("Timestamp must be > 0");
        }

        double value = buffer.getDouble();
        if (Double.isNaN(value)) {
            throw new MalformedTelemetryMessageException("Value must not be NaN");
        }

        int reserved = buffer.getInt();
        if (reserved != 0) {
            throw new MalformedTelemetryMessageException("Reserved must be 0");
        }

        return new TelemetryMessage(deviceId, metricId, timestamp, value);
    }


}
