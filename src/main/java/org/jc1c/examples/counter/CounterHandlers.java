package org.jc1c.examples.counter;

import org.jc1c.annotations.JHandler;
import org.jc1c.annotations.JHandlerControllers;

import java.time.Instant;

@JHandlerControllers
public class CounterHandlers {

    @JHandler(methodName = "plus double")
    public Integer methodPlus(Double value) {
        Counter.getInstance().plusIndex((int) value.doubleValue());
        return Counter.getInstance().getIndex();
    }

    @JHandler(methodName = "plus long")
    public Integer methodPlus1(Long value) {
        Counter.getInstance().plusIndex(value.intValue());
        return Counter.getInstance().getIndex();
    }

    @JHandler(methodName = "super plus")
    public Integer methodPlus(Long value, Instant instant) {
        Counter.getInstance().plusIndex(value.intValue());
        return Counter.getInstance().getIndex();
    }

    @JHandler(methodName = "minus")
    public Integer methodMinus(Double value) {
        Counter.getInstance().minusIndex((int) value.doubleValue());
        return Counter.getInstance().getIndex();
    }

    @JHandler(methodName = "minus long")
    public Integer methodMinus1(Long value) {
        Counter.getInstance().minusIndex(value.intValue());
        return Counter.getInstance().getIndex();
    }

    @JHandler(methodName = "get")
    public Integer methodMinus() {
        return Counter.getInstance().getIndex();
    }

}
