package com.pbkour.temil;

import com.pbkour.temil.telemetry.TelemetryEncoder;
import com.pbkour.temil.telemetry.TelemetryMessage;
import lombok.experimental.StandardException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public Client(String host, int port) {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress(host, port));

            sendObject(socketChannel, new TelemetryMessage(1, 1, 1, 1));
        } catch (IOException e) {
            throw new ClientException(e);
        }
    }

    void sendObject(SocketChannel channel, TelemetryMessage obj) throws IOException {
        byte[] bytes = TelemetryEncoder.encode(obj);

        ByteBuffer dataBuffer = ByteBuffer.wrap(bytes);
        while (dataBuffer.hasRemaining()) {
            channel.write(dataBuffer);
        }
    }

    @StandardException
    public static class ClientException extends RuntimeException {
    }
}
