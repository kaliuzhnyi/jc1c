package org.jc1c;

import java.util.Arrays;
import java.util.LinkedHashMap;

public class JServerOutMessage {

    private final JServerOutMessageTypes type;
    private LinkedHashMap<String, String> properties;

    {
        properties = new LinkedHashMap<>();
    }

    private JServerOutMessage(JServerOutMessageTypes type) {
        this.type = type;
    }

    public static Builder builder(JServerOutMessageTypes type) {
        return new Builder(type);
    }

    public static class Builder {

        private JServerOutMessage outMessage;

        private Builder(JServerOutMessageTypes type) {
            outMessage = new JServerOutMessage(type);
        }

        public Builder withProperties(String key, String value) {
            outMessage.addProperties(key, value);
            return this;
        }

        public JServerOutMessage build() {
            return outMessage;
        }

    }

    private void addProperties(String key, String value) {
        properties.put(key, value);
    }

    public JServerOutMessageTypes getType() {
        return type;
    }

    public LinkedHashMap<String, String> getProperties() {
        return properties;
    }

    public void print() {
        System.out.println(this);
    }

    public static void print(JServerOutMessage ... outMessages) {

        StringBuffer textBuffer = new StringBuffer();
        Arrays.stream(outMessages).toList().forEach(textBuffer::append);
        System.out.println(textBuffer);

    }

    @Override
    public String toString() {

        StringBuffer textBuffer = new StringBuffer();
        textBuffer.append("@Message: " + type + "\n");
        properties.entrySet().forEach(entry -> textBuffer.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n"));

        return textBuffer.toString();
    }

}