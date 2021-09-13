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

    @JHandler(methodName = "test with result and number parameter")
    public String testWithResultNumberParameter(Double value) {
        System.out.println("test method, with result " + value);
        return "test method, with result " + value;
    }

    @JHandler(methodName = "test with result and number, string parameter")
    public String testWithResultNumberParameter(Double value, String string) {
        System.out.println("test method, with result " + value + " " + string);
        return "test method, with result " + value + " " + string;
    }

}
