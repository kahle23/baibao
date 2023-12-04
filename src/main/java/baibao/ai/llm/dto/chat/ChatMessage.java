package baibao.ai.llm.dto.chat;

import java.io.Serializable;

public class ChatMessage extends Message implements Serializable {
    private String name;

    public ChatMessage(String role, String name, Object content) {
        super(role, content);
        this.name = name;
    }

    public ChatMessage(String role, Object content, String toolCallId, Object toolCalls) {

        super(role, content, toolCallId, toolCalls);
    }

    public ChatMessage(String role, Object content) {

        super(role, content);
    }

    public ChatMessage() {

    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

}
