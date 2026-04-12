package com.pbkour.temil.stats;

import com.sun.net.httpserver.HttpServer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class StatServer {
    private HttpServer server;

    public StatServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/stats", new StatEndpointHandler());
        server.setExecutor(null); // creates a default executor
        this.server = server;
    }

    public void start() {
        server.start();

    }

    public void stop() {
        server.stop(0);
    }

    public static void main(String[] args) throws IOException {
        StatServer statServer = new StatServer(6001);
        statServer.start();
    }
}
