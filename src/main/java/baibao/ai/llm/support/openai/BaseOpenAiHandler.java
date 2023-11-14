package baibao.ai.llm.support.openai;

import artoria.data.Dict;
import artoria.data.bean.BeanUtils;
import artoria.util.ObjectUtils;
import baibao.ai.llm.dto.embedding.EmbeddingData;
import baibao.ai.llm.dto.embedding.EmbeddingReq;
import baibao.ai.llm.dto.embedding.EmbeddingResp;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseOpenAiHandler extends AbstractOpenAiHandler {

    @Override
    public Object embedding(Object input, Class<?> clazz) {
        // Conversion input parameter.
        if (input instanceof EmbeddingReq) {
            EmbeddingReq req = (EmbeddingReq) input;
            input = Dict.of("input", req.getInput())
                    .set("model", req.getModel())
                    .set("encoding_format", req.getEncodingFormat())
                    .set("configCode", req.getConfigCode());
        }
        // Conversion output parameter.
        if (EmbeddingResp.class.equals(clazz)) {
            // It must be Dict.
            Dict embedding = (Dict) super.embedding(input, clazz);
            // Create EmbeddingDTO.
            List<EmbeddingData> dataList = new ArrayList<EmbeddingData>();
            EmbeddingResp result = new EmbeddingResp();
            result.setObject(embedding.getString("object"));
            result.setModel(embedding.getString("model"));
            result.setData(dataList);
            // Convert data.
            @SuppressWarnings("rawtypes")
            List data = (List) embedding.get("data");
            for (Object datum : data) {
                Dict dict = Dict.of(BeanUtils.beanToMap(datum));
                String  object = dict.getString("object");
                Integer index = dict.getInteger("index");
                List<Object> list = ObjectUtils.cast(dict.get("embedding"));
                dataList.add(new EmbeddingData(object, index, list));
            }
            // Result.
            return result;
        }
        else { return super.embedding(input, clazz); }
    }

}
