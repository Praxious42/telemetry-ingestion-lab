package com.pbkour.temil.stats;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;

public class StatEndpointHandler implements HttpHandler {
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = mapper.writeValueAsString(StatsDto.builder().messagesReceived(1).messagesProcessed(1).queueDepth(1).drops(1).uptimeSeconds(1).build());
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
