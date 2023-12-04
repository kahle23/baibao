package baibao.ai.llm.dto.chat;

import artoria.data.StreamDataHandler;

import java.io.Serializable;
import java.util.List;

public class ChatReq implements Serializable {
    private StreamDataHandler streamDataHandler;
    /**
     * A list of messages comprising the conversation so far.
     */
    private List<ChatMessage> messages;
    /**
     * The id of the model to use.
     */
    private String  model;
    private Integer maxTokens;
    /**
     * If set, partial message deltas will be sent.
     */
    private Boolean stream;
    /**
     * What sampling temperature to use.
     */
    private Number  temperature;
    private List<Tool> tools;
    private String  configCode;

    public ChatReq(List<ChatMessage> messages, String model, Boolean stream) {
        this.messages = messages;
        this.model = model;
        this.stream = stream;
    }

    public ChatReq(List<ChatMessage> messages, String model) {
        this.messages = messages;
        this.model = model;
    }

    public ChatReq() {

    }

    public StreamDataHandler getStreamDataHandler() {

        return streamDataHandler;
    }

    public void setStreamDataHandler(StreamDataHandler streamDataHandler) {

        this.streamDataHandler = streamDataHandler;
    }

    public List<ChatMessage> getMessages() {

        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {

        this.messages = messages;
    }

    public String getModel() {

        return model;
    }

    public void setModel(String model) {

        this.model = model;
    }

    public Integer getMaxTokens() {

        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {

        this.maxTokens = maxTokens;
    }

    public Boolean getStream() {

        return stream;
    }

    public void setStream(Boolean stream) {

        this.stream = stream;
    }

    public Number getTemperature() {

        return temperature;
    }

    public void setTemperature(Number temperature) {

        this.temperature = temperature;
    }

    public List<Tool> getTools() {

        return tools;
    }

    public void setTools(List<Tool> tools) {

        this.tools = tools;
    }

    public String getConfigCode() {

        return configCode;
    }

    public void setConfigCode(String configCode) {

        this.configCode = configCode;
    }

}
