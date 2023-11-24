package baibao.ai.llm.support.openai;

import artoria.common.constant.Symbols;
import artoria.data.Dict;
import artoria.data.StreamDataHandler;
import artoria.data.bean.BeanUtils;
import artoria.data.json.JsonUtils;
import artoria.util.Assert;
import artoria.util.ObjectUtils;
import baibao.ai.llm.dto.chat.ChatResp;
import baibao.ai.llm.dto.chat.Choice;
import baibao.ai.llm.dto.chat.Message;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StreamConvertHandler implements StreamDataHandler {
    private final StreamDataHandler streamDataHandler;

    public StreamConvertHandler(StreamDataHandler streamDataHandler) {

        this.streamDataHandler = streamDataHandler;
    }

    public StreamDataHandler getStreamDataHandler() {

        return streamDataHandler;
    }

    @Override
    public void handle(Object... arguments) {
        String line = arguments[4] != null ? String.valueOf(arguments[4]) : null;
        if (needConvert(line)) {
            ChatResp chatResp = convertLine(line);
            line = "data: " + JsonUtils.toJsonString(chatResp) + Symbols.LINE_FEED;
        }
        arguments[4] = line;
        getStreamDataHandler().handle(arguments);
    }

    private boolean needConvert(String line) {
        if (StrUtil.isBlank(line)) { return false; }
        line = line.trim();
        if ("data: [DONE]".equals(line)) { return false; }
        return line.startsWith("data: ");
    }

    private ChatResp convertLine(String line) {
        Assert.notBlank(line, "line not blank !");
        line = line.trim();
        Assert.isTrue(line.startsWith("data: "), "line must start with \"data: \". ");
        Assert.isFalse("data: [DONE]".equals(line), "line must not equals \"data: [DONE]\". ");
        String substring = line.substring("data: ".length());

        Dict chatDict = JsonUtils.parseObject(substring, Dict.class);
        // Create ChatResp.
        ChatResp result = BeanUtils.mapToBean(chatDict, ChatResp.class);
        result.setChoices(new ArrayList<Choice>());
        // Convert choices.
        @SuppressWarnings("rawtypes")
        List choices = (List) chatDict.get("choices");
        for (Object choice : choices) {
            Dict choiceDict = Dict.of(BeanUtils.beanToMap(choice));
            String  object = choiceDict.getString("finish_reason");
            Integer index = choiceDict.getInteger("index");
            Map<String, Object> deltaMap = ObjectUtils.cast(choiceDict.get("delta"));
            Message message = BeanUtils.mapToBean(deltaMap, Message.class);
            message.setToolCalls(deltaMap.get("tool_calls"));
            result.getChoices().add(new Choice(index, message, object));
        }

        return result;
    }

}
