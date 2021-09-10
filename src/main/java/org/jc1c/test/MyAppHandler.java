package org.jc1c.test;

import org.jc1c.annotations.JHandler;
import org.jc1c.annotations.JHandlersController;

@JHandlersController
public class MyAppHandler {

    @JHandler(methodName = "test")
    public void test() {
        System.out.println("test method, do smth");
    }

}
