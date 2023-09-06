package baibao.ai.llm.dto.embedding;

import java.io.Serializable;
import java.util.List;

public class EmbeddingData implements Serializable {
    private String  object;
    private Integer index;
    private List<Object> embedding;

    public EmbeddingData(String object, Integer index, List<Object> embedding) {
        this.embedding = embedding;
        this.object = object;
        this.index = index;
    }

    public EmbeddingData() {

    }

    public String getObject() {

        return object;
    }

    public void setObject(String object) {

        this.object = object;
    }

    public Integer getIndex() {

        return index;
    }

    public void setIndex(Integer index) {

        this.index = index;
    }

    public List<Object> getEmbedding() {

        return embedding;
    }

    public void setEmbedding(List<Object> embedding) {

        this.embedding = embedding;
    }

}
