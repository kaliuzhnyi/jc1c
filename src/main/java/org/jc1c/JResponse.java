package org.jc1c;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JResponse {

    private HashMap<String, Object> parameters;

    public JResponse() {
        parameters = new HashMap<>(1);
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private JResponse jResponse;

        private Builder() {
            jResponse = new JResponse();
        }

        public Builder withParameter(String key, Object value) {
            jResponse.addParameters(key, value);
            return this;
        }

        public JResponse build() {
            return jResponse;
        }

    }


    public HashMap<String, Object> getParameters() {
        return parameters;
    }

    public boolean hasParameters() {
        return !parameters.isEmpty();
    }

    public void addParameters(String key, Object value) {
        parameters.put(key, value);
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

            JsonObject jsonObject = new JsonObject();

            JsonElement resultJsonObject = JsonNull.INSTANCE;
            if (jResponse.hasParameters()) {
                JsonObject parametersJsonObject = new JsonObject();
                for (Map.Entry<String, Object> entry : jResponse.getParameters().entrySet()) {

                    Object value = entry.getValue();
                    if (value instanceof Boolean) {
                        parametersJsonObject.addProperty(entry.getKey(), (Boolean) entry.getValue());
                    } else if (value instanceof Number) {
                        parametersJsonObject.addProperty(entry.getKey(), (Number) entry.getValue());
                    } else if (value instanceof String) {
                        parametersJsonObject.addProperty(entry.getKey(), (String) entry.getValue());
                    } else if (value instanceof JResponse) {
                        parametersJsonObject.add(entry.getKey(), jsonSerializationContext.serialize(value));
                    }

                }

                resultJsonObject = parametersJsonObject;
            }

            jsonObject.add("result", resultJsonObject);

            return jsonObject;
        }

    }

}
