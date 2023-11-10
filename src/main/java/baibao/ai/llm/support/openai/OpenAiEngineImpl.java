package baibao.ai.llm.support.openai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Proxy;

/**
 * The ai engine based on the openai api.
 * @see <a href="https://platform.openai.com/docs/api-reference">API REFERENCE</a>
 * @author Kahle
 */
public class OpenAiEngineImpl extends BaseOpenAiEngine {
    private static final Logger log = LoggerFactory.getLogger(OpenAiEngineImpl.class);
    private final Config config;

    public OpenAiEngineImpl(String apiKey, Proxy proxy) {

        this.config = new Config(apiKey, proxy);
    }

    public OpenAiEngineImpl(String apiKey) {

        this.config = new Config(apiKey);
    }

    @Override
    protected Config getConfig(Object arguments) {

        return config;
    }

}
