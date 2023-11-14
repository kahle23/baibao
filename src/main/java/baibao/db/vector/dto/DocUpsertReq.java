package baibao.db.vector.dto;

import java.util.List;

public class DocUpsertReq {
    private String collection;
    private String partition;
    private List<DocBasicData> documents;
    private String configCode;

    public String getCollection() {

        return collection;
    }

    public void setCollection(String collection) {

        this.collection = collection;
    }

    public String getPartition() {

        return partition;
    }

    public void setPartition(String partition) {

        this.partition = partition;
    }

    public List<DocBasicData> getDocuments() {

        return documents;
    }

    public void setDocuments(List<DocBasicData> documents) {

        this.documents = documents;
    }

    public String getConfigCode() {

        return configCode;
    }

    public void setConfigCode(String configCode) {

        this.configCode = configCode;
    }

}
