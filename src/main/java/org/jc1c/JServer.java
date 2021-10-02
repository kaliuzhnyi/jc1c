package org.jc1c;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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

        public Builder withHostname(String hostname) {
            if (Objects.nonNull(hostname)) {
                jServer.hostname = hostname;
            }
            return this;
        }

        public Builder withPort(Integer port) {
            if (Objects.nonNull(port)) {
                jServer.port = port;
            }
            return this;
        }

        public Builder withApiKey(String apiKey) {
            if (Objects.nonNull(apiKey)) {
                jServer.apiKey = apiKey;
            }
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
            if (Objects.nonNull(limitTime)) {
                jServer.handlersProcessingTimeController = jServer.new HandlersProcessingTimeController(limitTime);
            }
            return this;
        }

        public JServer build() throws IOException {

            jServer.httpServer = HttpServer.create(new InetSocketAddress(jServer.hostname, jServer.port), jServer.backlog);
            jServer.httpServer.setExecutor(Executors.newFixedThreadPool(jServer.threadPool));
            jServer.httpServer.createContext("/", new JContextHandler());

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


    public HandlersProcessingTimeController getHandlersProcessingTimeController() {
        return handlersProcessingTimeController;
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
    }

    public void stop() {
        httpServer.stop(0);
        System.exit(0);
    }

}
