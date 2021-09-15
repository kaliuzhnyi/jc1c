package org.jc1c;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class JRequest {

    private String methodName;
    private List<Object> parameters;


    private JRequest() {
        parameters = new ArrayList<>();
    }


    public String getMethodName() {
        return methodName;
    }

    public boolean hasMethodName() {
        return !methodName.isBlank();
    }

    public boolean checkMethodName(String methodName) {
        return this.methodName.equalsIgnoreCase(methodName);
    }

    private void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public List<Object> getParameters() {
        return parameters;
    }

    public boolean hasParameters() {
        return getParametersCount() > 0;
    }

    private void addParameters(Object parameter) {
        parameters.add(parameter);
    }


    public Integer getParametersCount() {
        return parameters.size();
    }

    public boolean checkParametersCount(Integer count) {
        return getParametersCount().equals(count);
    }

    public List<Class<?>> getParameterTypes() {
        List<Class<?>> result = new ArrayList<>();
        getParameters().stream().forEach(parameter -> result.add(parameter.getClass()));
        return result;
    }

    public boolean checkParameterTypes(List<Class<?>> parameterTypesForCheck) {

        if (Objects.isNull(parameterTypesForCheck)) {
            return false;
        }

        List<Class<?>> parameterTypes = getParameterTypes();
        for (int i = 0; i < parameterTypesForCheck.size(); i++) {
            if (!parameterTypesForCheck.get(i).equals(parameterTypes.get(i))) {
                return false;
            }
        }

        return true;

    }


    public static JRequest fromJson(String json) {
        return new GsonBuilder()
                .registerTypeAdapter(JRequest.class, new JRequestTypeAdapter())
                .serializeNulls()
                .create()
                .fromJson(json, JRequest.class);
    }

    private static class JRequestTypeAdapter implements JsonDeserializer<JRequest> {

        @Override
        public JRequest deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

            JRequest request = new JRequest();

            if (!jsonElement.isJsonObject()) {
                return request;
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("method")) {
                request.setMethodName(jsonObject.get("method").getAsString());
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

                        Double doubleValue = primitive.getAsDouble();
                        if (doubleValue % 1 > 0.) {
                            request.addParameters(primitive.getAsDouble());
                        } else {
                            request.addParameters(primitive.getAsLong());
                        }

                    } else if (primitive.isString()) {

                        String stringValue = primitive.getAsString();

                        try {
                            Instant instant = Instant.parse(stringValue);
                            request.addParameters(instant);
                        } catch (DateTimeParseException e) {
                            request.addParameters(stringValue);
                        }

                    }

                }

            }

            return request;
        }

    }

}
