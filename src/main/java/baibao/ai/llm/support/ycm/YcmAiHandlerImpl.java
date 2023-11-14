package baibao.ai.llm.support.ycm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ai engine based on the openai api.
 * @see <a href="https://platform.openai.com/docs/api-reference">API REFERENCE</a>
 * @author Kahle
 */
public class YcmAiHandlerImpl extends AbstractYcmAiHandler {
    private static final Logger log = LoggerFactory.getLogger(YcmAiHandlerImpl.class);
    private final Config config;

    public YcmAiHandlerImpl(String accessKey, String secretKey, Long modelId) {

        this.config = new Config(accessKey, secretKey, modelId);
    }

    public YcmAiHandlerImpl(String accessKey, String secretKey) {

        this.config = new Config(accessKey, secretKey);
    }

    @Override
    protected Config getConfig(Object input) {

        return config;
    }

}
