package baibao.ai.splitter.support;

import artoria.ai.support.AbstractClassicAiHandler;
import artoria.util.Assert;
import baibao.ai.splitter.dto.TextSplitReq;
import baibao.ai.splitter.dto.TextSplitResp;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static artoria.common.constant.Numbers.ONE;
import static artoria.common.constant.Numbers.ZERO;
import static java.lang.Boolean.FALSE;

public abstract class BaseSimpleTextSplitter extends AbstractClassicAiHandler {

    protected abstract Config getConfig(Object input);

    @Override
    public Object execute(Object input, String strategy, Class<?> clazz) {
        //
        Assert.isSupport(clazz, FALSE, TextSplitResp.class);
        if (input == null) { return Collections.emptyList(); }
        Assert.isSupport(input.getClass(), FALSE, TextSplitReq.class, String.class);
        //
        List<String> separators = null;
        Integer chunkSize = null;
        String text;
        if (input instanceof TextSplitReq) {
            TextSplitReq req = (TextSplitReq) input;
            separators = req.getSeparators();
            chunkSize = req.getChunkSize();
            text = req.getText();
        }
        else { text = String.valueOf(input); }
        if (StrUtil.isBlank(text)) { return Collections.emptyList(); }
        //
        Config config = getConfig(input);
        if (separators == null) { separators = config.getSeparators(); }
        if (chunkSize == null) { chunkSize = config.getChunkSize(); }
        TextSplitResp resp = new TextSplitResp(new ArrayList<String>());
        // Get existing separator.
        String separator = null;
        for (String str : separators) {
            if (str == null) { continue; }
            if (text.indexOf(str) > ZERO) {
                separator = str; break;
            }
        }
        // Split text.
        List<String> splitTexts = separator != null
                ? Arrays.asList(text.split(separator)) : Collections.singletonList(text);
        for (String splitText : splitTexts) {
            if (StrUtil.isBlank(splitText)) { continue; }
            int length = splitText.length();
            if (length <= chunkSize) {
                resp.getSplitTexts().add(splitText);
                continue;
            }
            int count = length / chunkSize + (length % chunkSize > ZERO ? ONE : ZERO);
            for (int i = ZERO; i < count; i++) {
                int beginIndex = i * chunkSize;
                int endIndex = (i + ONE) * chunkSize;
                if (endIndex > length) { endIndex = length; }
                resp.getSplitTexts().add(text.substring(beginIndex, endIndex));
            }
        }
        // The result.
        return resp;
    }

    public static class Config implements Serializable {
        /**
         * Follow the list to find if a separator is included, split with the first existing separator.
         */
        private List<String> separators;
        /**
         * This is the size of each chunk.
         */
        private Integer chunkSize;

        public Config(List<String> separators, Integer chunkSize) {
            this.separators = separators;
            this.chunkSize = chunkSize;
        }

        public Config(Integer chunkSize) {

            this.chunkSize = chunkSize;
        }

        public Config() {

        }

        public List<String> getSeparators() {

            return separators;
        }

        public void setSeparators(List<String> separators) {

            this.separators = separators;
        }

        public Integer getChunkSize() {

            return chunkSize;
        }

        public void setChunkSize(Integer chunkSize) {

            this.chunkSize = chunkSize;
        }

    }

}
