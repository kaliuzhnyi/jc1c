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

        JServer.builder()
                .withHostname("localhost")
                .withPort(8080)
                .withBacklog(3)
                .withThreadPool(3)
                .withHandlersController(CounterHandlers.class)
                .build()
                .start();

    }

}
