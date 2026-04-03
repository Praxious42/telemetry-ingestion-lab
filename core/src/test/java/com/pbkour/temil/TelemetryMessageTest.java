package com.pbkour.temil;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.*;

class TelemetryMessageTest {
    @Test
    void should_decode_message_when_all_validations_pass() {
        byte[] bytes = frame(1L, 1, 1L, 50.0, 0);

        TelemetryMessage decoded = TelemetryDecoder.decode(bytes);

        assertEquals(1L, decoded.deviceId());
        assertEquals(1, decoded.metricId());
        assertEquals(1L, decoded.timestamp());
        assertEquals(50.0, decoded.value());
    }

    @Test
    void should_reject_message_if_frame_truncated() {
        ByteBuffer bufferBelowFrameSize = ByteBuffer.allocate(31).order(ByteOrder.BIG_ENDIAN);
        bufferBelowFrameSize.putLong(1L);
        bufferBelowFrameSize.putInt(0);
        bufferBelowFrameSize.putLong(1L);
        bufferBelowFrameSize.putDouble(1);

        assertThrows(IllegalArgumentException.class, () -> TelemetryDecoder.decode(bufferBelowFrameSize.array()));
    }

    @Test
    void should_reject_message_if_frame_above_frame_size() {
        ByteBuffer bufferAboveFrameSize = ByteBuffer.allocate(40).order(ByteOrder.BIG_ENDIAN);
        bufferAboveFrameSize.putLong(1L);
        bufferAboveFrameSize.putInt(0);
        bufferAboveFrameSize.putLong(1L);
        bufferAboveFrameSize.putDouble(1L);
        bufferAboveFrameSize.putLong(1L);

        assertThrows(IllegalArgumentException.class, () -> TelemetryDecoder.decode(bufferAboveFrameSize.array()));
    }

    @Test
    void should_reject_message_if_frame_malformed() {
        ByteBuffer bufferAboveFrameSize = ByteBuffer.allocate(32).order(ByteOrder.BIG_ENDIAN);
        bufferAboveFrameSize.putLong(1L);
        bufferAboveFrameSize.putInt(0);
        bufferAboveFrameSize.putLong(1L);


        assertThrows(MalformedTelemetryMessageException.class, () -> TelemetryDecoder.decode(bufferAboveFrameSize.array()));
    }

    @Test
    void should_reject_message_if_device_id_is_invalid() {
        byte[] bytes = frame(0L, 1, 1L, 50.0, 1);

        assertThrows(MalformedTelemetryMessageException.class, () -> TelemetryDecoder.decode(bytes));
    }

    @Test
    void should_reject_message_if_metric_id_is_invalid() {
        byte[] bytes = frame(1L, 0, 1L, 50.0, 1);

        assertThrows(MalformedTelemetryMessageException.class, () -> TelemetryDecoder.decode(bytes));
    }

    @Test
    void should_reject_message_if_timestamp_is_invalid() {
        byte[] bytes = frame(1L, 1, 0L, 50.0, 1);

        assertThrows(MalformedTelemetryMessageException.class, () -> TelemetryDecoder.decode(bytes));
    }

    @Test
    void should_reject_message_if_value_is_invalid() {
        byte[] bytes = frame(1L, 1, 1L, Double.NaN, 1);

        assertThrows(MalformedTelemetryMessageException.class, () -> TelemetryDecoder.decode(bytes));
    }

    @Test
    void should_reject_message_if_reserved_is_invalid() {
        byte[] bytes = frame(1L, 1, 1L, 50.0, 1);

        assertThrows(MalformedTelemetryMessageException.class, () -> TelemetryDecoder.decode(bytes));
    }

    private static byte[] frame(long deviceId, int metricId, long timestamp, double value, int reserved) {
        ByteBuffer buffer = ByteBuffer.allocate(TelemetryDecoder.FRAME_SIZE).order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(deviceId);
        buffer.putInt(metricId);
        buffer.putLong(timestamp);
        buffer.putDouble(value);
        buffer.putInt(reserved);
        return buffer.array();
    }
}