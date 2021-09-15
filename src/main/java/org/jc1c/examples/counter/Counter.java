package org.jc1c.examples.counter;

import org.jc1c.JServer;

import java.io.IOException;

public class Counter {

    private Integer index;

    private Counter() {
        index = 0;
    }

    private static class CounterHolder {
        public static Counter instance = new Counter();
    }

    public static Counter getInstance() {
        return CounterHolder.instance;
    }


    public Integer getIndex() {
        return index;
    }

    public void plusIndex(Integer value) {
        index += value;
    }

    public void minusIndex(Integer value) {
        index -= value;
    }


    public static void main(String[] args) throws IOException {

        String hostname = (args.length >= 1 && !args[0].isBlank())
                ? args[0]
                : null;

        Integer port = (args.length >= 2 && !args[1].isBlank())
                ? Integer.valueOf(args[1])
                : null;

        String apiKey = (args.length >= 3 && !args[2].isBlank())
                ? args[2]
                : null;

        JServer.builder()
                .withHostname(hostname)
                .withPort(port)
                .withApiKey(apiKey)
                .withBacklog(3)
                .withThreadPool(3)
                .withHandlersController(CounterHandlers.class)
                .build()
                .start();

    }

}
