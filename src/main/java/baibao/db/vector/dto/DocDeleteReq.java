package baibao.db.vector.dto;

import java.util.List;

public class DocDeleteReq {
    private String collection;
    private String partition;
    private List<String> ids;
    private Boolean deleteAll;
    private Object  filter;

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

    public List<String> getIds() {

        return ids;
    }

    public void setIds(List<String> ids) {

        this.ids = ids;
    }

    public Boolean getDeleteAll() {

        return deleteAll;
    }

    public void setDeleteAll(Boolean deleteAll) {

        this.deleteAll = deleteAll;
    }

    public Object getFilter() {

        return filter;
    }

    public void setFilter(Object filter) {

        this.filter = filter;
    }

}
