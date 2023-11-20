package baibao.db.vector.dto.document;

import java.util.Map;

public class DocFetchResp {
    private Map<String, DocBasicData> documents;
    private String collection;

    public DocFetchResp(String collection, Map<String, DocBasicData> documents) {
        this.collection = collection;
        this.documents = documents;
    }

    public DocFetchResp() {

    }

    public String getCollection() {

        return collection;
    }

    public void setCollection(String collection) {

        this.collection = collection;
    }

    public Map<String, DocBasicData> getDocuments() {

        return documents;
    }

    public void setDocuments(Map<String, DocBasicData> documents) {

        this.documents = documents;
    }

}
