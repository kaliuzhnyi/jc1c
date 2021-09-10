package org.jc1c;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JRequest {

    private String methodName;
    private List<Object> parameters;

    {
        parameters = new ArrayList<>();
    }

    private JRequest() {
    }

    public static JRequest fromJson(String json) {
        return new GsonBuilder()
                .registerTypeAdapter(JRequest.class, new JRequestTypeAdapter())
                .create()
                .fromJson(json, JRequest.class);
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean hasMethodName() {
        return !methodName.isBlank();
    }

    private void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public boolean hasParameters() {
        return !Objects.isNull(parameters) && !parameters.isEmpty();
    }

    private void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    private void addParameters(Object parameter) {
        parameters.add(parameter);
    }

    static class JRequestTypeAdapter implements JsonDeserializer<JRequest> {

        @Override
        public JRequest deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

            JRequest request = new JRequest();

            if (!jsonElement.isJsonObject()) {
                return request;
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("methodName")) {
                request.setMethodName(jsonObject.get("methodName").getAsString());
            }

            if (jsonObject.has("parameters")
                    && jsonObject.get("parameters").isJsonArray()) {

                JsonArray parametersJsonArray = jsonObject.getAsJsonArray("parameters");
                for (JsonElement element : parametersJsonArray) {

                    if (!element.isJsonPrimitive()) {
                        continue;
                    }

                    JsonPrimitive primitive = element.getAsJsonPrimitive();
                    if (primitive.isBoolean()) {
                        request.addParameters(primitive.getAsBoolean());
                    } else if (primitive.isNumber()) {
                        request.addParameters(primitive.getAsDouble());
                    } else if (primitive.isString()) {
                        request.addParameters(primitive.getAsString());
                    }

                }

            }

            return request;
        }

    }

}
