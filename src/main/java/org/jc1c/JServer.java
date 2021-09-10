package org.jc1c;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class JServer {

    private static final String DEFAULT_HTTP_SERVER_HOSTNAME = "localhost";
    private static final Integer DEFAULT_HTTP_SERVER_PORT = 8080;
    private static final Integer DEFAULT_HTTP_SERVER_BACKLOG = 3;

    private String hostname;
    private Integer port;
    private Integer backlog;

    private HttpServer httpServer;
    private Set<Class> handlers;

    private JServer() {
        hostname = DEFAULT_HTTP_SERVER_HOSTNAME;
        port = DEFAULT_HTTP_SERVER_PORT;
        backlog = DEFAULT_HTTP_SERVER_BACKLOG;
        handlers = new HashSet<>(1);
    }

    private static class JServerHolder {
        public static JServer instance = new JServer();
    }

    public static JServer getInstance() {
        return JServerHolder.instance;
    }

    public static class Builder {

        private JServer jServer;

        public Builder() {
            jServer = JServerHolder.instance;
        }

        public Builder withHostname(String hostname) {
            jServer.hostname = hostname;
            return this;
        }

        public Builder withPort(Integer port) {
            jServer.port = port;
            return this;
        }

        public Builder withBacklog(Integer backlog) {
            jServer.backlog = backlog;
            return this;
        }

        public Builder withHandler(Class cls) {
            jServer.handlers.add(cls);
            return this;
        }

        public JServer build() throws IOException {

            jServer.httpServer = HttpServer.create(new InetSocketAddress(jServer.hostname, jServer.port), jServer.backlog);
            jServer.httpServer.createContext("/", new JContextHandler());

            return jServer;
        }

    }

    public boolean hasHandlers() {
        return !handlers.isEmpty();
    }

    public Set<Class> getHandlers() {
        return handlers;
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

}
