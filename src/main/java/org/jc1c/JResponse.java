package org.jc1c;

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

}
