package com.pbkour.temil;

import com.pbkour.temil.telemetry.TelemetryEncoder;
import com.pbkour.temil.telemetry.TelemetryMessage;
import lombok.experimental.StandardException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.List;

public class Client {

    private final String host;
    private final int port;

    public Client(String host, int port) {
        if (host == null || host.isBlank()) {
            throw new ClientException("empty or null host");
        }

        if (port <= 0 || port > 65535) {
            throw new ClientException("invalid port " + port + ". Expected 0-65535");
        }

        this.host = host;
        this.port = port;
    }

    public void sendAll(List<TelemetryMessage> messages) throws IOException {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress(host, port));

            for (TelemetryMessage msg : messages) {
                byte[] bytes = TelemetryEncoder.encode(msg);
                ByteBuffer buf = ByteBuffer.wrap(bytes);
                while (buf.hasRemaining()) {
                    socketChannel.write(buf);
                }
                ByteBuffer readBuf = ByteBuffer.allocate(4);
                while (readBuf.hasRemaining()) {
                    int n = socketChannel.read(readBuf);
                    if (n == -1) {
                        readBuf.flip();
                        int response = readBuf.getInt();
                        System.out.println(response);
                        continue;
                    }
                }
            }




        }
    }

    @StandardException
    public static class ClientException extends RuntimeException {
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 6000);
        client.sendAll(List.of(new TelemetryMessage(1, 1, 1, 1)));
    }
}
