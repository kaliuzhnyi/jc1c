package org.jc1c;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

public final class JServer {

    private static final String DEFAULT_HTTP_SERVER_HOSTNAME = "localhost";
    private static final Integer DEFAULT_HTTP_SERVER_PORT = 8080;
    private static final Integer DEFAULT_HTTP_SERVER_BACKLOG = 3;
    private static final Integer DEFAULT_HTTP_SERVER_THREAD_POOL = 1;

    private String hostname;
    private Integer port;
    private Integer backlog;
    private Integer threadPool;

    private HttpServer httpServer;
    private Set<Class> handlerControllers;

    private JServer() {
        hostname = DEFAULT_HTTP_SERVER_HOSTNAME;
        port = DEFAULT_HTTP_SERVER_PORT;
        backlog = DEFAULT_HTTP_SERVER_BACKLOG;
        threadPool = DEFAULT_HTTP_SERVER_THREAD_POOL;
        handlerControllers = new HashSet<>(1);
    }


    private static class JServerHolder {
        public static JServer instance = new JServer();
    }

    public static JServer getInstance() {
        return JServerHolder.instance;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private JServer jServer;

        private Builder() {
            jServer = getInstance();
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

        public Builder withThreadPool(Integer threadPool) {
            jServer.threadPool = threadPool;
            return this;
        }

        public Builder withHandlersController(Class cls) {
            jServer.handlerControllers.add(cls);
            return this;
        }

        public JServer build() throws IOException {

            jServer.httpServer = HttpServer.create(new InetSocketAddress(jServer.hostname, jServer.port), jServer.backlog);
            jServer.httpServer.setExecutor(Executors.newFixedThreadPool(jServer.threadPool));
            jServer.httpServer.createContext("/", new JContextHandler());

            return jServer;
        }

    }


    public Set<Class> getHandlerControllers() {
        return handlerControllers;
    }

    public boolean hasHandlerControllers() {
        return !handlerControllers.isEmpty();
    }

    public String getHostname() {
        return hostname;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getBacklog() {
        return backlog;
    }

    public Integer getThreadPool() {
        return threadPool;
    }


    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

}
