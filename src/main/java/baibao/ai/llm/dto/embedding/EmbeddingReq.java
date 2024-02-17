/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.ai.llm.dto.embedding;

import java.io.Serializable;
import java.util.List;

public class EmbeddingReq implements Serializable {
    private List<String> input;
    private String model;
    private String encodingFormat;
    private String configCode;

    public EmbeddingReq(List<String> input, String model) {
        this.input = input;
        this.model = model;
    }

    public EmbeddingReq() {

    }

    public List<String> getInput() {

        return input;
    }

    public void setInput(List<String> input) {

        this.input = input;
    }

    public String getModel() {

        return model;
    }

    public void setModel(String model) {

        this.model = model;
    }

    public String getEncodingFormat() {

        return encodingFormat;
    }

    public void setEncodingFormat(String encodingFormat) {

        this.encodingFormat = encodingFormat;
    }

    public String getConfigCode() {

        return configCode;
    }

    public void setConfigCode(String configCode) {

        this.configCode = configCode;
    }

}
