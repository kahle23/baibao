package baibao.ai.splitter.dto;

import java.io.Serializable;
import java.util.List;

public class TextSplitResp implements Serializable {
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
