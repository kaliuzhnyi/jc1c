package org.jc1c;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class JServer {

    private static final String DEFAULT_HTTP_SERVER_HOSTNAME = "localhost";
    private static final Integer DEFAULT_HTTP_SERVER_PORT = 8080;
    private static final Integer DEFAULT_HTTP_SERVER_BACKLOG = 3;

    private final HttpServer httpServer;

    public JServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(DEFAULT_HTTP_SERVER_HOSTNAME, DEFAULT_HTTP_SERVER_PORT), DEFAULT_HTTP_SERVER_BACKLOG);
    }

    public JServer(Integer port) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(DEFAULT_HTTP_SERVER_HOSTNAME, port), DEFAULT_HTTP_SERVER_BACKLOG);
    }



}
