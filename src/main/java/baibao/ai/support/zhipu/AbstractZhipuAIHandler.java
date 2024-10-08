package baibao.ai.support.zhipu;

import baibao.ai.support.AbstractHttpApiAIHandler;
import kunlun.data.Dict;
import kunlun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static kunlun.common.constant.Numbers.FOUR;
import static kunlun.net.http.HttpMethod.POST;

public abstract class AbstractZhipuAIHandler extends AbstractHttpApiAIHandler {
    private static final Logger log = LoggerFactory.getLogger(AbstractZhipuAIHandler.class);

    /**
     * Get the AI handler configuration according to the arguments.
     * @param input The input parameters for inference calculations
     * @param operation The strategy or operation or null for AI handler execution
     * @param clazz The class of the return value
     * @return The AI handler configuration
     */
    protected abstract Config getConfig(Object input, String operation, Class<?> clazz);

    @Override
    public Object execute(Object input, String operation, Class<?> clazz) {
        if (StringUtils.isBlank(operation) || AIMethods.CHAT.equals(operation)) {
            return chat(input, operation, clazz);
        }
        else if (AIMethods.EMBEDDINGS.equals(operation)) {
            return embeddings(input, operation, clazz);
        }
        else {
            throw new UnsupportedOperationException(
                "The Zhipu AI Handler. \n" +
                "(The api documents \"https://maas.aminer.cn/dev/api\")\n" +
                "Supported method:\n" +
                " - chat\n" +
                " - embeddings\n"
            );
        }
    }

    protected Object chat(Object input, String operation, Class<?> clazz) {
        // Handle input.
        Dict inputDict = processInput(Tool.ME, input);
        // Get config.
        Config config = getConfig(inputDict, operation, clazz);
        // Invoke http.
        Dict respDict = doHttp(HttpData.of(Tool.ME).setConfig(config)
                .setHttpType(FOUR).setMethod(POST)
                .setUrl("https://open.bigmodel.cn/api/paas/v4/chat/completions")
                .setHeaders(Dict.of(AUTHORIZATION_KEY, BEARER_KEY + config.getApiKey()))
                .setData(inputDict));
        // Handle output.
        return processOutput(Tool.ME, respDict, clazz);
    }

    protected Object embeddings(Object input, String operation, Class<?> clazz) {
        // Handle input.
        Dict inputDict = processInput(Tool.ME, input);
        // Get config.
        Config config = getConfig(inputDict, operation, clazz);
        // Invoke http.
        Dict respDict = doHttp(HttpData.of(Tool.ME).setConfig(config)
                .setHttpType(FOUR).setMethod(POST)
                .setUrl("https://open.bigmodel.cn/api/paas/v4/embeddings")
                .setHeaders(Dict.of(AUTHORIZATION_KEY, BEARER_KEY + config.getApiKey()))
                .setData(inputDict));
        // Handle output.
        return processOutput(Tool.ME, respDict, clazz);
    }

    /**
     * The Zhipu AI Handler internal tool.
     * @author Kahle
     */
    protected static class Tool extends InnerTool {
        /**
         * The internal tool instance.
         */
        public static final Tool ME = new Tool();
    }

    /**
     * The Zhipu AI Handler configuration.
     * @author Kahle
     */
    public static class Config extends AbstractConfig {
        private String apiKey;

        public Config(String apiKey) {

            this.apiKey = apiKey;
        }

        public Config() {

        }

        public String getApiKey() {

            return apiKey;
        }

        public void setApiKey(String apiKey) {

            this.apiKey = apiKey;
        }
    }

}
