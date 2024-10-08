/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.ai.support.azure;

import baibao.ai.support.AbstractHttpApiAIHandler;
import kunlun.data.Dict;
import kunlun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static kunlun.common.constant.Numbers.FOUR;
import static kunlun.net.http.HttpMethod.POST;

/**
 * The Azure OpenAI AI Handler.
 * @see <a href="https://learn.microsoft.com/zh-cn/azure/ai-services/openai/reference">Azure OpenAI 服务 REST API 参考</a>
 * @author Kahle
 */
public abstract class AbstractAzureOpenAIHandler extends AbstractHttpApiAIHandler {
    private static final Logger log = LoggerFactory.getLogger(AbstractAzureOpenAIHandler.class);

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
                "The Azure OpenAI AI Handler. \n" +
                "(The api documents \"https://learn.microsoft.com/zh-cn/azure/ai-services/openai/reference\")\n" +
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
        // The model is equivalent to deployment-id.
        String url = String.format(
                "%s/openai/deployments/%s/chat/completions?api-version=%s"
                , Tool.ME.getEndpoint(config), inputDict.getString(MODEL_KEY), config.getApiVersion()
        );
        // Invoke http.
        Dict respDict = doHttp(HttpData.of(Tool.ME).setConfig(config).setHttpType(FOUR)
                .setValidateCertificate(false).setMethod(POST).setUrl(url)
                .setHeaders(Dict.of("api-key", config.getApiKey()))
                .setData(inputDict));
        // Handle output.
        return processOutput(Tool.ME, respDict, clazz);
    }

    protected Object embeddings(Object input, String operation, Class<?> clazz) {
        // Handle input.
        Dict inputDict = processInput(Tool.ME, input);
        // Get config.
        Config config = getConfig(inputDict, operation, clazz);
        // The model is equivalent to deployment-id.
        String url = String.format(
                "%s/openai/deployments/%s/embeddings?api-version=%s"
                , Tool.ME.getEndpoint(config), inputDict.getString(MODEL_KEY), config.getApiVersion()
        );
        // Invoke http.
        Dict respDict = doHttp(HttpData.of(Tool.ME).setConfig(config).setHttpType(FOUR)
                .setValidateCertificate(false).setMethod(POST).setUrl(url)
                .setHeaders(Dict.of("api-key", config.getApiKey()))
                .setData(inputDict));
        // Handle output.
        return processOutput(Tool.ME, respDict, clazz);
    }

    /**
     * The Azure Open AI handler internal tool.
     * @author Kahle
     */
    protected static class Tool extends InnerTool {
        /**
         * The internal tool instance.
         */
        public static final Tool ME = new Tool();

        public String getEndpoint(Config config) {
            String endpoint = config.getEndpoint();
            if (endpoint == null) { return null; }
            if (!endpoint.startsWith("https://")) {
                endpoint = "https://" + endpoint;
            }
            return endpoint;
        }
    }

    /**
     * The Azure Open AI handler configuration.
     * @author Kahle
     */
    public static class Config extends AbstractConfig {
        private String apiKey;
        private String endpoint;
        private String apiVersion;

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

        public String getEndpoint() {

            return endpoint;
        }

        public void setEndpoint(String endpoint) {

            this.endpoint = endpoint;
        }

        public String getApiVersion() {

            return apiVersion;
        }

        public void setApiVersion(String apiVersion) {

            this.apiVersion = apiVersion;
        }
    }

}
