package com.pbkour.temil;

import com.pbkour.temil.telemetry.TelemetryMessage;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ServerClientIntegrationTest {

    @Test
    void serverReceivesTelemetryMessageFromClient() throws Exception {
        int port = 6000;
        BlockingQueue<TelemetryMessage> queue = new LinkedBlockingQueue<>(1);

        Thread serverThread = new Thread(() -> new Server(queue, port));
        serverThread.setDaemon(true);
        serverThread.start();

        Thread.sleep(200);

        Thread clientThread = new Thread(() -> new Client("localhost", port));
        clientThread.start();

        TelemetryMessage received = queue.poll(2, TimeUnit.SECONDS);
        assertNotNull(received, "Server did not receive a TelemetryMessage within timeout");
        assertEquals(new TelemetryMessage(1, 1, 1, 1), received);
    }
}

