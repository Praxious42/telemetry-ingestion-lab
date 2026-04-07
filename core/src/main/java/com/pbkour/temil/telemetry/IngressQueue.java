package com.pbkour.temil.telemetry;

import lombok.experimental.StandardException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;

public class IngressQueue {
    private static final int CAPACITY = 64;
    private final ArrayBlockingQueue<TelemetryMessage> queue = new ArrayBlockingQueue<>(CAPACITY);

    public void enqueue(TelemetryMessage message) throws InterruptedException {
        if (message == null) {
            throw new IngressQueueException("message is null");
        }
        queue.put(message);
    }

    public void enqueue(List<TelemetryMessage> messages) throws InterruptedException {
        if (messages.stream().anyMatch(Objects::isNull)) {
            throw new IngressQueueException("found a message that is null");
        }

        for (TelemetryMessage message : messages) {
            queue.put(message);
        }
    }

    public TelemetryMessage dequeue() throws InterruptedException {
        return queue.take();
    }

    public List<TelemetryMessage> dequeue(int numberOfMessages) throws InterruptedException {
        if (numberOfMessages <= 0) {
            throw new IngressQueueException("number of messages to dequeue are less or equal to zero");

        }
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
