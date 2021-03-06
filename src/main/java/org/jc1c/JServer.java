package org.jc1c;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;

public final class JServer {

    private static final String DEFAULT_HTTP_SERVER_HOSTNAME = "localhost";
    private static final Integer DEFAULT_HTTP_SERVER_PORT = 8080;
    public static final String DEFAULT_HTTP_SERVER_APIKEY_HEADER = "API-key";

    private static final Integer DEFAULT_HTTP_SERVER_BACKLOG = 3;
    private static final Integer DEFAULT_HTTP_SERVER_THREAD_POOL = 1;

    private String hostname;
    private Integer port;
    private String apiKey;

    private Integer backlog;
    private Integer threadPool;

    private HttpServer httpServer;
    private Set<Class> handlerControllers;

    private HandlersProcessingTimeController handlersProcessingTimeController;

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

        private final JServer jServer;

        private Builder() {
            jServer = getInstance();
        }

        public Builder withArgs(String... args) {

            if (args.length >= 1 && !args[0].isBlank()) {
                withHostname(args[0]);
                if (args.length >= 2 && !args[1].isBlank()) {
                    withPort(Integer.valueOf(args[1]));
                    if (args.length >= 3 && !args[2].isBlank()) {
                        withApiKey(args[2]);
                        if (args.length >=4 && !args[3].isBlank()) {
                            withHandlersProcessingTimeController(Long.valueOf(args[3]));
                        }
                    }
                }
            }

            return this;
        }

        public Builder withHostname(String hostname) {
            jServer.hostname = hostname;
            return this;
        }

        public Builder withPort(Integer port) {
            jServer.port = port;
            return this;
        }

        public Builder withApiKey(String apiKey) {
            jServer.apiKey = apiKey;
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

        public Builder withHandlersProcessingTimeController(Long limitTime) {
            jServer.handlersProcessingTimeController = jServer.new HandlersProcessingTimeController(limitTime);
            return this;
        }

        public JServer build() throws IOException {

            InetSocketAddress inetSocketAddress = new InetSocketAddress(jServer.hostname, jServer.port);

            jServer.httpServer = HttpServer.create(inetSocketAddress, jServer.backlog);
            jServer.httpServer.setExecutor(Executors.newFixedThreadPool(jServer.threadPool));
            jServer.httpServer.createContext("/", new JContextHandler());

            if (jServer.port == 0) {
                jServer.port = jServer.httpServer.getAddress().getPort();
            }

            return jServer;
        }

    }


    public class HandlersProcessingTimeController implements Runnable {

        private volatile short processingCount = 0;
        private volatile Instant lastProcessingTime;

        private Long limitTime;

        private HandlersProcessingTimeController(Long limitTime) {
            setLimitTime(limitTime);
        }

        private void setLimitTime(Long limitTime) {
            if (limitTime <= 0) {
                throw new IllegalArgumentException("The limit time must be greater than 0");
            }
            this.limitTime = limitTime;
        }


        public synchronized void fixHandlerProcessingBegin() {
            processingCount++;
        }

        public synchronized void fixHandlerProcessingEnd() {
            processingCount--;
            fixLastProcessingTime();
        }


        private void fixLastProcessingTime() {
            lastProcessingTime = Instant.now();
        }


        private synchronized boolean limitTimeIsOver() {
            return processingCount == 0 && (Objects.isNull(lastProcessingTime) || lastProcessingTime.plusSeconds(limitTime).isBefore(Instant.now()));
        }

        @Override
        public void run() {
            fixLastProcessingTime();
            while (true) {
                if (limitTimeIsOver()) {
                    stop();
                    return;
                }
            }

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


    public String getApiKey() {
        return apiKey;
    }

    public boolean hasApiKey() {
        return !Objects.isNull(apiKey) && !apiKey.isEmpty();
    }

    public boolean checkApiKey(String apiKeyFoCheck) {
        if (!hasApiKey()) {
            return false;
        }
        return apiKey.equals(apiKeyFoCheck);
    }


    public Integer getBacklog() {
        return backlog;
    }

    public Integer getThreadPool() {
        return threadPool;
    }


    public void fixHandlerProcessingBegin() {
        if (Objects.nonNull(handlersProcessingTimeController)) {
            handlersProcessingTimeController.fixHandlerProcessingBegin();
        }
    }

    public void fixHandlerProcessingEnd() {
        if (Objects.nonNull(handlersProcessingTimeController)) {
            handlersProcessingTimeController.fixHandlerProcessingEnd();
        }
    }


    public void start() {
        httpServer.start();

        if (Objects.nonNull(handlersProcessingTimeController)) {
            Thread thread = new Thread(handlersProcessingTimeController);
            thread.setDaemon(true);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }

        JServerOutMessage connectionMessage = JServerOutMessage.builder(JServerOutMessageTypes.CONNECTION_DATA)
                .withProperties("Host", httpServer.getAddress().getHostName())
                .withProperties("Port", Integer.valueOf(httpServer.getAddress().getPort()).toString())
                .build();

        JServerOutMessage.print(connectionMessage);

    }

    public void stop() {
        httpServer.stop(0);
        System.exit(0);
    }

}
