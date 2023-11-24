package baibao.ai.llm.dto.chat;

public class Choice {
    private String  finishReason;
    private Message message;
    private Integer index;

    public Choice(Integer index, Message message, String finishReason) {
        this.finishReason = finishReason;
        this.message = message;
        this.index = index;
    }

    public Choice(Message message) {

        this.message = message;
    }

    public Choice() {

    }

    public Integer getIndex() {

        return index;
    }

    public void setIndex(Integer index) {

        this.index = index;
    }

    public Message getMessage() {

        return message;
    }

    public void setMessage(Message message) {

        this.message = message;
    }

    public String getFinishReason() {

        return finishReason;
    }

    public void setFinishReason(String finishReason) {

        this.finishReason = finishReason;
    }

}
