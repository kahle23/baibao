/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.ai.llm.dto.embedding;

import java.io.Serializable;
import java.util.List;

public class EmbeddingResp implements Serializable {
    private String id;
    private String object;
    private String model;
    private Long   created;
    private List<EmbeddingData> data;

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getObject() {

        return object;
    }

    public void setObject(String object) {

        this.object = object;
    }

    public String getModel() {

        return model;
    }

    public void setModel(String model) {

        this.model = model;
    }

    public Long getCreated() {

        return created;
    }

    public void setCreated(Long created) {

        this.created = created;
    }

    public List<EmbeddingData> getData() {

        return data;
    }

    public void setData(List<EmbeddingData> data) {

        this.data = data;
    }

}
