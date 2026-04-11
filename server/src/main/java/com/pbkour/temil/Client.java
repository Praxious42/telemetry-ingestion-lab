package com.pbkour.temil;

import com.pbkour.temil.telemetry.TelemetryEncoder;
import com.pbkour.temil.telemetry.TelemetryMessage;
import lombok.experimental.StandardException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
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

                readResponse(socketChannel);
            }
        }
    }

    private void readResponse(SocketChannel socketChannel) throws IOException {
        ByteBuffer readBuf = ByteBuffer.allocate(4);
        while (readBuf.hasRemaining()) {
            int n = socketChannel.read(readBuf);
            if (n == -1) {
                throw new IOException("Unexpected end of stream while waiting for server response");
            }
        }
        readBuf.flip();
        readBuf.getInt();
    }

    @StandardException
    public static class ClientException extends RuntimeException {
    }
}
