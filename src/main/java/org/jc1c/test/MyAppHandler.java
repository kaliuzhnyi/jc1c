package org.jc1c.test;

import org.jc1c.annotations.JHandler;
import org.jc1c.annotations.JHandlerControllers;

@JHandlerControllers
public class MyAppHandler {

    @JHandler(methodName = "test")
    public void test() {
        System.out.println("test method, do smth");
    }

    @JHandler(methodName = "test with result")
    public String testWithResult() {
        return "hello, i am result";
    }

}
