package com.pbkour.temil.telemetry;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IngressQueueTest {
    @Test
    void producersAndConsumersShouldWorkConcurrently() throws Exception {
        final int PRODUCERS = 4;
        final int CONSUMERS = 4;
        final int MESSAGES_PER_PRODUCER = 500;

        IngressQueue ingress = new IngressQueue();

        ExecutorService consumerPool = Executors.newFixedThreadPool(CONSUMERS);
        ExecutorService producerPool = Executors.newFixedThreadPool(PRODUCERS);

        AtomicInteger consumedCount = new AtomicInteger(0);

        List<Future<?>> producerFutures = new ArrayList<>();
        for (int p = 0; p < PRODUCERS; p++) {
            final int producerId = p;
            Future<?> f = producerPool.submit(() -> {
                try {
                    for (int i = 0; i < MESSAGES_PER_PRODUCER; i++) {
                        TelemetryMessage msg = new TelemetryMessage(producerId, i, System.currentTimeMillis(), i * 1.0);
                        ingress.enqueue(msg);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            producerFutures.add(f);
        }

        List<Future<?>> consumerFutures = new ArrayList<>();
        for (int c = 0; c < CONSUMERS; c++) {
            Future<?> f = consumerPool.submit(() -> {
                try {
                    while (true) {
                        TelemetryMessage msg = ingress.dequeue();
                        if (msg.deviceId() == -1L) {
                            break;
                        }

                        System.out.println(Thread.currentThread().getName() + " got: " + msg);
                        consumedCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            consumerFutures.add(f);
        }

        // Wait for producers to finish
        producerPool.shutdown();
        boolean producersFinished = producerPool.awaitTermination(30, TimeUnit.SECONDS);
        if (!producersFinished) {
            producerPool.shutdownNow();
            throw new RuntimeException("Producers did not finish in time");
        }

        for (int i = 0; i < CONSUMERS; i++) {
            ingress.enqueue(new TelemetryMessage(-1L, 0, 0L, 0.0));
        }

        // Wait for consumers to finish
        consumerPool.shutdown();
        boolean consumersFinished = consumerPool.awaitTermination(30, TimeUnit.SECONDS);
        if (!consumersFinished) {
            consumerPool.shutdownNow();
            throw new RuntimeException("Consumers did not finish in time");
        }

        int expected = PRODUCERS * MESSAGES_PER_PRODUCER;
        assertEquals(expected, consumedCount.get(), "All messages should have been consumed");
    }
}



