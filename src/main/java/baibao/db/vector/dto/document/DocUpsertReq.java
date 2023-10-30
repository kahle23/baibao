package baibao.db.vector.dto.document;

import java.io.Serializable;
import java.util.List;

public class DocUpsertReq implements Serializable {
    private String collection;
    private List<DocData> documents;
    private String configCode;

    public String getCollection() {

        return collection;
    }

    public void setCollection(String collection) {

        this.collection = collection;
    }

    public List<DocData> getDocuments() {

        return documents;
    }

    public void setDocuments(List<DocData> documents) {

        this.documents = documents;
    }

    public String getConfigCode() {

        return configCode;
    }

    public void setConfigCode(String configCode) {

        this.configCode = configCode;
    }

}
