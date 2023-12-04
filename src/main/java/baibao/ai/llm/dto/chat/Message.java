package baibao.ai.llm.dto.chat;

import java.io.Serializable;

public class Message implements Serializable {
    /**
     * tool calls
     */
    private Object toolCalls;
    private String toolCallId;
    /**
     * content
     */
    private Object content;
    /**
     * system, user, assistant, tool
     */
    private String role;

    public Message(String role, Object content, String toolCallId, Object toolCalls) {
        this.toolCallId = toolCallId;
        this.toolCalls = toolCalls;
        this.content = content;
        this.role = role;
    }

    public Message(String role, Object content) {
        this.role = role;
        this.content = content;
    }

    public Message() {

    }

    public String getRole() {

        return role;
    }

    public void setRole(String role) {

        this.role = role;
    }

    public Object getContent() {

        return content;
    }

    public void setContent(Object content) {

        this.content = content;
    }

    public String getToolCallId() {
        return toolCallId;
    }

    public void setToolCallId(String toolCallId) {
        this.toolCallId = toolCallId;
    }

    public Object getToolCalls() {

        return toolCalls;
    }

    public void setToolCalls(Object toolCalls) {

        this.toolCalls = toolCalls;
    }

}
