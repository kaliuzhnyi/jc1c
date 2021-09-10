package org.jc1c;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class JContextHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JRequest request = JRequest.fromJson(requestBody);

        if (!request.hasMethodName()) {
            sendResponseMethodNotFound(exchange);
            return;
        }

        JServer jServer = JServer.getInstance();
        if (!jServer.hasHandlers()) {
            sendResponseMethodNotFound(exchange);
            return;
        }

        for (Class handlersController : jServer.getHandlers()) {

            Method[] methodHandlers = (Method[]) Arrays.stream(handlersController.getDeclaredMethods())
                    .filter(method -> {return true;})
                    .toArray();

            if (!(methodHandlers.length > 0)) {
                continue;
            }

//            Method methodHandler = methodHandlers[0];
//            methodHandler.invoke();

        }

    }

    private void sendResponseMethodNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
        exchange.close();
    }

}
