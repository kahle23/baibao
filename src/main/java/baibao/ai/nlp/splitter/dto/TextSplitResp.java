package baibao.ai.nlp.splitter.dto;

import java.util.List;

public class TextSplitResp {
    private List<String> splitTexts;

    public TextSplitResp(List<String> splitTexts) {

        this.splitTexts = splitTexts;
    }

    public TextSplitResp() {

    }

    public List<String> getSplitTexts() {

        return splitTexts;
    }

    public void setSplitTexts(List<String> splitTexts) {

        this.splitTexts = splitTexts;
    }

}
