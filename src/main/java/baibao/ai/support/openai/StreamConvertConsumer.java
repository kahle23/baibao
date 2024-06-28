/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.ai.support.openai;

import cn.hutool.core.util.StrUtil;
import kunlun.ai.support.model.ChatResponse;
import kunlun.ai.support.model.Message;
import kunlun.ai.support.model.Usage;
import kunlun.common.constant.Symbols;
import kunlun.core.function.Consumer;
import kunlun.data.Dict;
import kunlun.data.bean.BeanUtils;
import kunlun.data.json.JsonUtils;
import kunlun.util.Assert;
import kunlun.util.ObjectUtils;

import java.util.List;
import java.util.Map;

public class StreamConvertConsumer implements Consumer<Object> {
    private final Consumer<Object> streamConsumer;

    public StreamConvertConsumer(Consumer<Object> streamConsumer) {

        this.streamConsumer = streamConsumer;
    }

    public Consumer<Object> getStreamConsumer() {

        return streamConsumer;
    }

    @Override
    public void accept(Object param) {
        String line = param != null ? String.valueOf(param) : null;
        if (needConvert(line)) {
            ChatResponse chatResp = convertLine(line);
            line = "data: " + JsonUtils.toJsonString(chatResp) + Symbols.LINE_FEED;
        }
        getStreamConsumer().accept(line);
    }

    private boolean needConvert(String line) {
        if (StrUtil.isBlank(line)) { return false; }
        line = line.trim();
        if ("data: [DONE]".equals(line)) { return false; }
        return line.startsWith("data: ");
    }

    private ChatResponse convertLine(String line) {
        Assert.notBlank(line, "line not blank !");
        line = line.trim();
        Assert.isTrue(line.startsWith("data: "), "line must start with \"data: \". ");
        Assert.isFalse("data: [DONE]".equals(line), "line must not equals \"data: [DONE]\". ");
        String substring = line.substring("data: ".length());

        Dict respDict = JsonUtils.parseObject(substring, Dict.class);
        // Create ChatResp.
        ChatResponse.Builder builder = ChatResponse.Builder.of();
        builder.setId(respDict.getString("id"));
        builder.setModel(respDict.getString("model"));
        // Convert choices.
        @SuppressWarnings("rawtypes")
        List choices = (List) respDict.get("choices");
        for (Object choice : choices) {
            Dict    choiceDict = Dict.of(BeanUtils.beanToMap(choice));
            String  object = choiceDict.getString("finish_reason");
            Integer index = choiceDict.getInteger("index");
            Map<String, Object> deltaMap = ObjectUtils.cast(choiceDict.get("delta"));
            Message message = BeanUtils.mapToBean(deltaMap, Message.class);
//            message.setToolCalls(deltaMap.get("tool_calls"));
            builder.addChoice(index, message, object);
        }
        // Convert usage.
        Dict usageDict = Dict.of(BeanUtils.beanToMap(respDict.get("usage")));
        builder.setUsage(Usage.Builder.of()
                .setPromptTokens(usageDict.getInteger("prompt_tokens"))
                .setCompletionTokens(usageDict.getInteger("completion_tokens"))
                .setTotalTokens(usageDict.getInteger("total_tokens"))
                .build()
        );
        // Build.
        return builder.build();
    }

}
