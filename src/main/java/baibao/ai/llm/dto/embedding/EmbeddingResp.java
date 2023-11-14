package baibao.ai.llm.dto.embedding;

import java.util.List;

public class EmbeddingResp {
    private String id;
    private String object;
    private String model;
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

    public List<EmbeddingData> getData() {

        return data;
    }

    public void setData(List<EmbeddingData> data) {

        this.data = data;
    }

}
