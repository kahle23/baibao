/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.db.vector.dto.document;

import java.io.Serializable;
import java.util.List;

public class DocQueryResp implements Serializable {
    private String collection;
    private String requestId;
    private List<DocQueryData> documents;

    public DocQueryResp(String collection, List<DocQueryData> documents) {
        this.collection = collection;
        this.documents = documents;
    }

    public DocQueryResp() {

    }

    public String getCollection() {

        return collection;
    }

    public void setCollection(String collection) {

        this.collection = collection;
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
