package org.jc1c.examples.counter;

import org.jc1c.annotations.JHandler;
import org.jc1c.annotations.JHandlerControllers;

@JHandlerControllers
public class CounterHandlers {

    @JHandler(methodName = "plus")
    public Integer methodPlus(Double value) {
        Counter.getInstance().plusIndex((int) value.doubleValue());
        return Counter.getInstance().getIndex();
    }

    @JHandler(methodName = "minus")
    public Integer methodMinus(Double value) {
        Counter.getInstance().minusIndex((int) value.doubleValue());
        return Counter.getInstance().getIndex();
    }

    @JHandler(methodName = "get")
    public Integer methodMinus() {
        return Counter.getInstance().getIndex();
    }

}
