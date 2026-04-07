package com.pbkour.temil.telemetry;

import lombok.experimental.StandardException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class IngressQueue {
    private static final int CAPACITY = 64;
    private final ArrayBlockingQueue<TelemetryMessage> queue = new ArrayBlockingQueue<>(CAPACITY);

    public void  enqueue(TelemetryMessage message) throws InterruptedException {
        queue.put(message);
    }

    public void enqueue(List<TelemetryMessage> messages) throws InterruptedException {
        for (TelemetryMessage message : messages) {
            queue.put(message);
        }
    }

    public TelemetryMessage dequeue() throws InterruptedException {
        return queue.take();
    }

    public List<TelemetryMessage> dequeue(int numberOfMessages) throws InterruptedException {
        List<TelemetryMessage> messages = new ArrayList<>();

        for (int i = 0; i < numberOfMessages; i++) {
            messages.add(queue.take());
        }

        return messages;
    }

    @StandardException
    public static class IngressQueueException extends RuntimeException {
    }
}
