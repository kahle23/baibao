package baibao.ai.llm.support.openai;

import baibao.ai.llm.dto.chat.*;
import baibao.ai.llm.dto.embedding.EmbeddingData;
import baibao.ai.llm.dto.embedding.EmbeddingReq;
import baibao.ai.llm.dto.embedding.EmbeddingResp;
import cn.hutool.core.collection.CollUtil;
import kunlun.data.Array;
import kunlun.data.Dict;
import kunlun.data.StreamDataHandler;
import kunlun.data.bean.BeanUtils;
import kunlun.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseOpenAiHandler extends AbstractOpenAiHandler {

    @Override
    public Object chat(Object input, Class<?> clazz) {
        // Conversion input parameter.
        Boolean stream = null;
        if (input instanceof ChatReq) {
            ChatReq req = (ChatReq) input;
            // Handle messages.
            List<ChatMessage> messages = req.getMessages();
            Array msgArray = Array.of();
            for (ChatMessage message : messages) {
                Dict dict = Dict.of("role", message.getRole())
                        .set("content", message.getContent());
                if (message.getName() != null) {
                    dict.set("name", message.getName());
                }
                if (message.getToolCallId() != null) {
                    dict.set("tool_call_id", message.getToolCallId());
                }
                if (message.getToolCalls() != null) {
                    dict.set("tool_calls", message.getToolCalls());
                }
                msgArray.add(dict);
            }
            //
            stream = req.getStream();
            StreamDataHandler streamHandler = req.getStreamDataHandler();
            if (stream != null && stream && ChatResp.class.isAssignableFrom(clazz)) {
                if (streamHandler != null) {
                    streamHandler = new StreamConvertHandler(streamHandler);
                }
            }
            // Handle req.
            Dict inputDict = Dict.of("streamDataHandler", streamHandler)
                    .set("messages", msgArray)
                    .set("model", req.getModel())
                    .set("max_tokens", req.getMaxTokens())
                    .set("stream", stream)
                    .set("temperature", req.getTemperature())
                    .set("configCode", req.getConfigCode())
            ;
            List<Tool> tools = req.getTools();
            if (CollUtil.isNotEmpty(tools)) { inputDict.set("tools", tools); }
            input = inputDict;
        }
        // Conversion output parameter.
        if ((stream == null || !stream) && ChatResp.class.isAssignableFrom(clazz)) {
            // It must be Dict.
            Dict chatDict = (Dict) super.chat(input, clazz);
            // Create ChatResp.
            ChatResp result = BeanUtils.mapToBean(chatDict, ChatResp.class);
            result.setChoices(new ArrayList<Choice>());
            // Convert choices.
            @SuppressWarnings("rawtypes")
            List choices = (List) chatDict.get("choices");
            for (Object choice : choices) {
                Dict    choiceDict = Dict.of(BeanUtils.beanToMap(choice));
                String  object = choiceDict.getString("finish_reason");
                Integer index = choiceDict.getInteger("index");
                Map<String, Object> messageMap = ObjectUtils.cast(choiceDict.get("message"));
                Message message = BeanUtils.mapToBean(messageMap, Message.class);
                message.setToolCalls(messageMap.get("tool_calls"));
                result.getChoices().add(new Choice(index, message, object));
            }
            // Result.
            return result;
        }
        else { return super.chat(input, clazz); }
    }

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
        if (EmbeddingResp.class.isAssignableFrom(clazz)) {
            // It must be Dict.
            Dict embedding = (Dict) super.embedding(input, clazz);
            // Create EmbeddingResp.
            EmbeddingResp result = new EmbeddingResp();
            result.setObject(embedding.getString("object"));
            result.setModel(embedding.getString("model"));
            result.setCreated(embedding.getLong("created"));
            result.setData(new ArrayList<EmbeddingData>());
            // Convert data.
            @SuppressWarnings("rawtypes")
            List data = (List) embedding.get("data");
            for (Object datum : data) {
                Dict dict = Dict.of(BeanUtils.beanToMap(datum));
                String  object = dict.getString("object");
                Integer index = dict.getInteger("index");
                List<Object> list = ObjectUtils.cast(dict.get("embedding"));
                result.getData().add(new EmbeddingData(object, index, list));
            }
            // Result.
            return result;
        }
        else { return super.embedding(input, clazz); }
    }

}
