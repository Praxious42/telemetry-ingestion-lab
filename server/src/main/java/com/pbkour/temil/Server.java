package com.pbkour.temil;

import com.pbkour.temil.telemetry.TelemetryDecoder;
import com.pbkour.temil.telemetry.TelemetryMessage;
import lombok.experimental.StandardException;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.concurrent.BlockingQueue;

public class Server {
    public Server(BlockingQueue<TelemetryMessage> queue, int port) {
        if (queue == null) {
            throw new ServerException("queue is null");
        }
        if (port <= 0 || port > 65535) {
            throw new ServerException("invalid port " + port + ". Expected 0-65535");
        }

        try (ServerSocketChannel channel = ServerSocketChannel.open()) {
            channel.bind(new InetSocketAddress(port));
            while (true) {
                readMessagesFromChannel(queue, channel);
            }
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }

    private void readMessagesFromChannel(BlockingQueue<TelemetryMessage> queue, ServerSocketChannel channel) {
        try (SocketChannel client = channel.accept()) {
            while (true) {
                TelemetryMessage telemetryMessage = receiveObject(client);
                try {
                    queue.put(telemetryMessage);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ServerException(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TelemetryMessage receiveObject(SocketChannel channel) throws IOException {
        ByteBuffer dataBuffer = ByteBuffer.allocate(TelemetryDecoder.FRAME_SIZE);
        while (dataBuffer.hasRemaining()) {
            if (channel.read(dataBuffer) == -1) {
                throw new EOFException("Incomplete data received");
            }
        }

        return TelemetryDecoder.decode(dataBuffer.array());
    }

    @StandardException
    public static class ServerException extends RuntimeException {
    }
}
