package org.jc1c;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class JResponse {

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
                .serializeNulls()
                .create()
                .toJson(this);
    }

    private static class JResponseTypeAdapter implements JsonSerializer<JResponse> {

        @Override
        public JsonElement serialize(JResponse jResponse, Type type, JsonSerializationContext jsonSerializationContext) {

            JsonObject jsonObject = new JsonObject();

            if (!jResponse.hasParameters()) {
                serializeParameter(jsonObject, "result", null, jsonSerializationContext);
            } else if (jResponse.getParameters().containsKey("result")) {
                serializeParameter(jsonObject, "result", jResponse.getParameters().get("result"), jsonSerializationContext);
            } else {

                JsonObject parametersJsonObject = new JsonObject();
                jResponse.getParameters().entrySet().stream().forEach(entry -> {
                    serializeParameter(parametersJsonObject, entry.getKey(), entry.getValue(), jsonSerializationContext);
                });

                jsonObject.add("result", parametersJsonObject);
            }

            return jsonObject;
        }

        private void serializeParameter(JsonObject jsonObject, String key, Object value, JsonSerializationContext jsonSerializationContext) {

            if (Objects.isNull(value)) {
                jsonObject.add(key, JsonNull.INSTANCE);
            } else if (value instanceof Boolean) {
                jsonObject.addProperty(key, (Boolean) value);
            } else if (value instanceof Number) {
                jsonObject.addProperty(key, (Number) value);
            } else if (value instanceof String) {
                jsonObject.addProperty(key, (String) value);
            } else if (value instanceof JResponse) {
                jsonObject.add(key, jsonSerializationContext.serialize(value));
            }

        }


    }

}
