package org.jc1c;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jc1c.annotations.JHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class JContextHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        JMethods jMethod = JMethods.valueOf(exchange.getRequestMethod());
        switch (jMethod) {
            case GET:
                handleGet(exchange);
                break;
            case POST:
                handlePost(exchange);
                break;
            case DELETE:
                handleDelete(exchange);
                break;
            default:
                handleUnknown(exchange);
        }

    }

    public void handleGet(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 200);
    }

    public void handlePost(HttpExchange exchange) throws IOException {

        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JRequest request = JRequest.fromJson(requestBody);

        if (!request.hasMethodName()) {
            sendResponseMethodNotFound(exchange);
            return;
        }

        JServer jServer = JServer.getInstance();
        if (!jServer.hasHandlerControllers()) {
            sendResponseMethodNotFound(exchange);
            return;
        }

        for (Class<?> handlersController : jServer.getHandlerControllers()) {

            List<Method> methods = Arrays.stream(handlersController.getDeclaredMethods())
                    .filter(method -> {
                        JHandler jHandler = method.getAnnotation(JHandler.class);
                        return !Objects.isNull(jHandler)
                                && request.checkMethodName(jHandler.methodName())
                                && request.checkParametersCount(method.getParameterCount())
                                && request.checkParameterTypes(List.of(method.getParameterTypes()));
                        // Arrays.equals(method.getParameterTypes(), request.getParameterTypes().toArray())
                    }).collect(Collectors.toList());

            if (!(methods.size() > 0)) {
                continue;
            }

            try {

                Object obj = handlersController.getDeclaredConstructor().newInstance();
                Method method = methods.get(0);

                Object result = method.invoke(obj, request.getParameters().toArray());
                JResponse jResponse = JResponse.builder().withParameter("result", result).build();
                sendResponse(exchange, 200, jResponse.toJson());
                return;

            } catch (Exception e) {
                e.printStackTrace();
                sendResponseMethodNotCreated(exchange);
                return;
            }

        }

        sendResponseMethodNotFound(exchange);

    }

    public void handleDelete(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 200);
        JServer.getInstance().stop();
        System.exit(0);
    }

    public void handleUnknown(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 405);
    }


    private void sendResponseMethodNotFound(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 404);
    }

    private void sendResponseMethodNotCreated(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 500);
    }

    private void sendResponseMethodInvoked(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 200);
    }

    private void sendResponse(HttpExchange exchange, Integer code, String body) throws IOException {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bodyBytes.length);
        exchange.getResponseBody().write(bodyBytes);
        exchange.close();
    }

    private void sendResponse(HttpExchange exchange, Integer code) throws IOException {
        exchange.sendResponseHeaders(code, 0);
        exchange.close();
    }

}
