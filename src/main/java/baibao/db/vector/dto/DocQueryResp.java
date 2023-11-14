package baibao.db.vector.dto;

import java.util.List;

public class DocQueryResp {
    private String collection;
    private String partition;
    private String requestId;
    private List<DocQueryData> documents;

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

    public String getRequestId() {

        return requestId;
    }

    public void setRequestId(String requestId) {

        this.requestId = requestId;
    }

    public List<DocQueryData> getDocuments() {

        return documents;
    }

    public void setDocuments(List<DocQueryData> documents) {

        this.documents = documents;
    }

}
