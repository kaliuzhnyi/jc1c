package org.jc1c.test;

import org.jc1c.annotations.JHandler;
import org.jc1c.annotations.JHandlerControllers;

@JHandlerControllers
public class MyAppHandler {

    @JHandler(methodName = "test")
    public void test() {
        System.out.println("test method, without result");
    }

    @JHandler(methodName = "test with result")
    public String testWithResult() {
        System.out.println("test method, with result");
        return "test method, with result";
    }

}
