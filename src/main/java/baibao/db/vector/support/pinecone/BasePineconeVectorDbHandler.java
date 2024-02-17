package baibao.db.vector.support.pinecone;

import baibao.db.vector.dto.document.*;
import kunlun.convert.ConversionUtils;
import kunlun.data.Dict;
import kunlun.data.bean.BeanUtils;
import kunlun.util.ObjectUtils;

import java.util.*;

public abstract class BasePineconeVectorDbHandler extends AbstractPineconeVectorDbHandler {

    @Override
    public Object docQuery(Object condition, Class<?> clazz) {
        // Conversion input parameter.
        if (condition instanceof DocQueryReq) {
            DocQueryReq req = (DocQueryReq) condition;
            condition = Dict.of("namespace", req.getCollection())
                    .set("id", req.getId())
                    .set("vector", req.getVector())
                    .set("filter", req.getFilter())
                    .set("topK", req.getTopK())
                    .set("includeValues", req.getIncludeVector())
                    .set("includeMetadata", true)
                    .set("configCode", req.getConfigCode())
            ;
        }
        // Conversion output parameter.
        if (DocQueryResp.class.isAssignableFrom(clazz)) {
            // It must be Dict.
            Dict docQuery = (Dict) super.docQuery(condition, clazz);
            // Create DocQueryResp.
            List<DocQueryData> documents = new ArrayList<DocQueryData>();
            DocQueryResp result = new DocQueryResp(
                    docQuery.getString("namespace"), documents);
            // Convert data.
            @SuppressWarnings("rawtypes")
            List matches = (List) docQuery.get("matches");
            matches = matches != null ? matches : Collections.emptyList();
            for (Object datum : matches) {
                Dict dict = Dict.of(BeanUtils.beanToMap(datum));
                String id = dict.getString("id");
                Object score = dict.get("score");
                List<Object> values = ObjectUtils.cast(dict.get("values"));
                Map<Object, Object> metadata = ObjectUtils.cast(dict.get("metadata"));
                //
                DocQueryData docQueryData = new DocQueryData(id, values, metadata);
                docQueryData.setScore(score);
                documents.add(docQueryData);
            }
            return result;
        }
        else { return super.docQuery(condition, clazz); }
    }

    @Override
    public Object docDelete(Object condition, Class<?> clazz) {
        // Conversion input parameter.
        if (condition instanceof DocDeleteReq) {
            DocDeleteReq req = (DocDeleteReq) condition;
            condition = Dict.of("namespace", req.getCollection())
                    .set("ids", req.getIds())
                    .set("deleteAll", req.getDeleteAll())
                    .set("filter", req.getFilter())
                    .set("configCode", req.getConfigCode())
            ;
        }
        return super.docDelete(condition, clazz);
    }

    @Override
    public Object docFetch(Object condition, Class<?> clazz) {
        // Conversion input parameter.
        if (condition instanceof DocFetchReq) {
            DocFetchReq req = (DocFetchReq) condition;
            condition = Dict.of("namespace", req.getCollection())
                    .set("ids", req.getIds())
                    .set("configCode", req.getConfigCode())
            ;
        }
        // Conversion output parameter.
        if (DocFetchResp.class.isAssignableFrom(clazz)) {
            // It must be Dict.
            Dict docFetch = (Dict) super.docFetch(condition, clazz);
            // Create DocFetchResp.
            Map<String, DocData> documents = new LinkedHashMap<String, DocData>();
            DocFetchResp result = new DocFetchResp(
                    docFetch.getString("namespace"), documents);
            // Convert data.
            Map<String, Object> vectors = ObjectUtils.cast(docFetch.get("vectors"));
            vectors = vectors != null ? vectors : Collections.<String, Object>emptyMap();
            for (Map.Entry<String, Object> entry : vectors.entrySet()) {
                Dict dict = Dict.of(BeanUtils.beanToMap(entry.getValue()));
                String id = dict.getString("id");
                List<Object> values = ObjectUtils.cast(dict.get("values"));
                Map<Object, Object> metadata = ObjectUtils.cast(dict.get("metadata"));
                documents.put(id, new DocData(id, values, metadata));
            }
            return result;
        }
        else { return super.docFetch(condition, clazz); }
    }

    @Override
    public Object docUpsert(Object data, Class<?> clazz) {
        // Conversion input parameter.
        if (data instanceof DocUpsertReq) {
            DocUpsertReq req = (DocUpsertReq) data;
            // Get document data.
            List<DocData> documents = req.getDocuments();
            documents = documents != null ? documents : Collections.<DocData>emptyList();
            // convert vectors
            List<Dict> vectors = new ArrayList<Dict>();
            for (DocData docData : documents) {
                if (docData == null) { continue; }
                Dict dict = Dict.of("id", docData.getId())
                        .set("values", docData.getVector())
                        .set("metadata", docData.getData());
                vectors.add(dict);
            }
            //
            data = Dict.of("namespace", req.getCollection())
                    .set("vectors", vectors)
                    .set("configCode", req.getConfigCode());
        }
        // Conversion output parameter.
        if (Number.class.isAssignableFrom(clazz)) {
            Dict docUpsert = (Dict) super.docUpsert(data, clazz);
            return ConversionUtils.convert(docUpsert.get("upsertedCount"), clazz);
        }
        else { return super.docUpsert(data, clazz); }
    }

}
