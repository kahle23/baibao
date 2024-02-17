/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.ai.llm.dto.chat;

import java.io.Serializable;

public class Tool implements Serializable {

    public static Tool ofFunction(String type, String name, String description, Object parameters) {
        Tool tool = new Tool(type);
        tool.setFunction(new Function(name, description, parameters));
        return tool;
    }


    /**
     * The type of the tool. Currently, only function is supported.
     */
    private String type;

    private Function function;

    public Tool(String type) {

        this.type = type;
    }

    public Tool() {

    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public Function getFunction() {

        return function;
    }

    public void setFunction(Function function) {

        this.function = function;
    }

    public static class Function implements Serializable {
        /**
         * The name of the function to be called.
         */
        private String name;
        /**
         * A description of what the function does,
         *      used by the model to choose when and how to call the function.
         */
        private String description;
        /**
         * The parameters the functions accepts.
         */
        private Object parameters;

        public Function(String name, String description, Object parameters) {
            this.description = description;
            this.parameters = parameters;
            this.name = name;
        }

        public Function() {

        }

        public String getName() {

            return name;
        }

        public void setName(String name) {

            this.name = name;
        }

        public String getDescription() {

            return description;
        }

        public void setDescription(String description) {

            this.description = description;
        }

        public Object getParameters() {

            return parameters;
        }

        public void setParameters(Object parameters) {

            this.parameters = parameters;
        }

    }

}
