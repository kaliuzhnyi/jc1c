package org.jc1c.test;

import org.jc1c.JServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class MyApp {

    public static void main(String[] args) throws IOException {

        JServer.builder()
                .withHostname("localhost")
                .withPort(8080)
                .withBacklog(3)
                .withThreadPool(3)
                .withHandlersController(MyAppHandler.class)
                .build()
                .start();

    }

}
