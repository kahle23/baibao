package baibao.db.vector.support.pinecone;

import artoria.convert.ConversionUtils;
import artoria.data.Dict;
import artoria.data.bean.BeanUtils;
import artoria.util.ObjectUtils;
import baibao.db.vector.dto.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
                    .set("configCode", req.getConfigCode());
        }
        // Conversion output parameter.
        if (DocQueryResp.class.isAssignableFrom(clazz)) {
            // It must be Dict.
            Dict docQuery = (Dict) super.docQuery(condition, clazz);
            // Create DocQueryResp.
            List<DocQueryData> documents = new ArrayList<DocQueryData>();
            DocQueryResp result = new DocQueryResp();
            result.setCollection(docQuery.getString("namespace"));
            result.setDocuments(documents);
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
            ;
        }
        return super.docDelete(condition, clazz);
    }

    @Override
    public Object docUpsert(Object data, Class<?> clazz) {
        // Conversion input parameter.
        if (data instanceof DocUpsertReq) {
            DocUpsertReq req = (DocUpsertReq) data;
            // Get document data.
            List<DocBasicData> documents = req.getDocuments();
            documents = documents != null ? documents : Collections.<DocBasicData>emptyList();
            // convert vectors
            List<Dict> vectors = new ArrayList<Dict>();
            for (DocBasicData docData : documents) {
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
