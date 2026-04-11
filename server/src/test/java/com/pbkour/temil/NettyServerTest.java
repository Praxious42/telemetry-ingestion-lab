package com.pbkour.temil;

import com.pbkour.temil.aggregate.AggregateStore;
import com.pbkour.temil.telemetry.TelemetryMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

class NettyServerTest {

    @Test
    void test() throws Exception {
        int port = 6000;
        AggregateStore aggregateStore = new AggregateStore();
        Thread thread = new Thread(() -> {
            try {

                NettyServer nettyServer = new NettyServer(port, aggregateStore);
                nettyServer.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        thread.setDaemon(true);
        thread.start();

        Thread.sleep(500);


        Client client = new Client("localhost", port);

        List<TelemetryMessage> messages = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            messages.add(new TelemetryMessage(i+1, 1, 1, 1));
        }

        client.sendAll(messages);

        for (int i = 0; i < 3; i++) {
            Thread.sleep(1000);
            if (aggregateStore.getSize() == 10000) {
                return;
            }
        }
        fail("Did not get the size in time");
    }

}