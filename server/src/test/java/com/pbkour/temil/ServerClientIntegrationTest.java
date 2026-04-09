package com.pbkour.temil;

import com.pbkour.temil.telemetry.TelemetryMessage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ServerClientIntegrationTest {

    @Test
    void serverReceivesTelemetryMessageFromClient() throws Exception {
        int port = 6000;
        BlockingQueue<TelemetryMessage> queue = new LinkedBlockingQueue<>(100000);

        Thread serverThread = new Thread(() -> new Server(queue, port));
        serverThread.setDaemon(true);
        serverThread.start();

        Thread.sleep(200);

        List<TelemetryMessage> messages = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            messages.add(new TelemetryMessage(i+1, 1, 1, 1));
        }

        long start = Instant.now().toEpochMilli();
        System.out.println(start);
        Instant end;

        Client client = new Client("localhost", port);
        client.sendAll(messages);

        Thread.sleep(5000);
        System.out.println(queue.size());
        assertEquals(100_000, queue.size());
    }
}

