package org.jc1c;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JContextHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JRequest request = JRequest.fromJson(requestBody);

        if (!request.hasMethodName()) {
            sendResponseMethodNotFound(exchange);
            return;
        }



    }

    private void sendResponseMethodNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
        exchange.close();
    }

}
