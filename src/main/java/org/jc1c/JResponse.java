package org.jc1c;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.security.PublicKey;
import java.util.ArrayList;

public class JResponse {

    private ArrayList<Object> parameters;

    public JResponse() {
        parameters = new ArrayList<>();
    }


    public Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private JResponse jResponse;

        private Builder() {
            jResponse = new JResponse();
        }

        public Builder withParameter(Object parameter) {
            jResponse.addParameters(parameter);
            return this;
        }

        public JResponse build() {
            return jResponse;
        }

    }


    public ArrayList<Object> getParameters() {
        return parameters;
    }

    public boolean hasParameters() {
        return !parameters.isEmpty();
    }

    public void addParameters(Object parameter) {
        parameters.add(parameter);
    }


    public String toJson() {
        return new GsonBuilder()
                .registerTypeAdapter(JResponse.class, new JResponseTypeAdapter())
                .create()
                .toJson(this);
    }

    private static class JResponseTypeAdapter implements JsonSerializer<JResponse> {

        @Override
        public JsonElement serialize(JResponse jResponse, Type type, JsonSerializationContext jsonSerializationContext) {
            return null;
        }

    }

}
