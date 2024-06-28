/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.ai.support.openai;

import baibao.ai.support.AbstractHttpApiAIHandler;
import kunlun.data.Dict;
import kunlun.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static kunlun.common.constant.Numbers.*;
import static kunlun.net.http.HttpMethod.GET;
import static kunlun.net.http.HttpMethod.POST;

/**
 * The OpenAI AI Handler.
 * @see <a href="https://platform.openai.com/docs/api-reference">API REFERENCE</a>
 * @author Kahle
 */
public abstract class AbstractOpenAIHandler extends AbstractHttpApiAIHandler {
    private static final Logger log = LoggerFactory.getLogger(AbstractOpenAIHandler.class);

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
        if ("chat".equals(operation)) {
            return chat(input, operation, clazz);
        }
        else if ("embeddings".equals(operation)) {
            return embeddings(input, operation, clazz);
        }
        else if ("speechCreate".equals(operation)) {
            return speechCreate(input, operation, clazz);
        }
        else if ("transcriptionCreate".equals(operation)) {
            return transcriptionCreate(input, operation, clazz);
        }
        else if ("translationCreate".equals(operation)) {
            return translationCreate(input, operation, clazz);
        }
        else if ("completion".equals(operation)) {
            return completion(input, operation, clazz);
        }
        else if ("imageCreate".equals(operation)) {
            return imageCreate(input, operation, clazz);
        }
        else if ("imageEdit".equals(operation)) {
            return imageEdit(input, operation, clazz);
        }
        else if ("imageVariation".equals(operation)) {
            return imageVariation(input, operation, clazz);
        }
        else if ("modelList".equals(operation)) {
            return modelList(input, operation, clazz);
        }
        else {
            throw new UnsupportedOperationException(
                "The OpenAI AI Handler. \n" +
                "(The api documents \"https://platform.openai.com/docs/api-reference\")\n" +
                "Supported method:\n" +
                " - chat\n" +
                " - embedding\n" +
                " - speechCreate\n" +
                " - transcriptionCreate\n" +
                " - translationCreate\n" +
                " - completion\n" +
                " - imageCreate\n" +
                " - imageEdit\n" +
                " - imageVariation\n" +
                " - modelList\n"
            );
        }
    }

    /**
     * Given a list of messages comprising a conversation, the model will return a response.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/chat/create">
     *     Create chat completion</a>
     */
    protected Object chat(Object input, String operation, Class<?> clazz) {
        // Handle input.
        Dict inputDict = processInput(Tool.ME, input);
        // Get config.
        Config config = getConfig(inputDict, operation, clazz);
        // Invoke http.
        Dict respDict = doHttp(HttpData.of(Tool.ME).setConfig(config)
                .setMethod(POST).setHttpType(FOUR)
                .setUrl("https://api.openai.com/v1/chat/completions")
                .setHeaders(Dict.of(AUTHORIZATION_KEY, BEARER_KEY + config.getApiKey()))
                .setData(inputDict));
        // Handle output.
        return processOutput(Tool.ME, respDict, clazz);
    }

    /**
     * Get a vector representation of a given input that can be easily consumed by
     *      machine learning models and algorithms.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/embeddings/create">
     *     Create embeddings</a>
     */
    protected Object embeddings(Object input, String operation, Class<?> clazz) {
        // Handle input.
        Dict inputDict = processInput(Tool.ME, input);
        // Get config.
        Config config = getConfig(inputDict, operation, clazz);
        // Invoke http.
        Dict respDict = doHttp(HttpData.of(Tool.ME).setConfig(config)
                .setMethod(POST).setHttpType(FOUR)
                .setUrl("https://api.openai.com/v1/embeddings")
                .setHeaders(Dict.of(AUTHORIZATION_KEY, BEARER_KEY + config.getApiKey()))
                .setData(inputDict));
        // Handle output.
        return processOutput(Tool.ME, respDict, clazz);
    }

    /**
     * Generates audio from the input text.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/audio/createSpeech">
     *     Create speech</a>
     */
    protected Object speechCreate(Object input, String operation, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input = Tool.ME.toDict(input), operation, clazz);
        return doHttp(HttpData.of(Tool.ME).setConfig(config)
                .setMethod(POST).setHttpType(FOUR)
                .setUrl("https://api.openai.com/v1/audio/speech")
                .setHeaders(Dict.of(AUTHORIZATION_KEY, BEARER_KEY + config.getApiKey()))
                .setData((Dict) input));
    }

    /**
     * Transcribes audio into the input language.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/audio/createTranscription">
     *     Create transcription</a>
     */
    protected Object transcriptionCreate(Object input, String operation, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input = Tool.ME.toDict(input), operation, clazz);
        return doHttp(HttpData.of(Tool.ME).setConfig(config)
                .setMethod(POST).setHttpType(FOUR)
                .setUrl("https://api.openai.com/v1/audio/transcriptions")
                .setHeaders(Dict.of(AUTHORIZATION_KEY, BEARER_KEY + config.getApiKey()))
                .setData((Dict) input));
    }

    /**
     * Translates audio into English.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/audio/createTranslation">
     *     Create translation</a>
     */
    protected Object translationCreate(Object input, String operation, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input = Tool.ME.toDict(input), operation, clazz);
        return doHttp(HttpData.of(Tool.ME).setConfig(config)
                .setMethod(POST).setHttpType(FOUR)
                .setUrl("https://api.openai.com/v1/audio/translations")
                .setHeaders(Dict.of(AUTHORIZATION_KEY, BEARER_KEY + config.getApiKey()))
                .setData((Dict) input));
    }

    /**
     * Given a prompt, the model will return one or more predicted completions,
     *      and can also return the probabilities of alternative tokens at each position.
     *      We recommend most users use our Chat Completions API.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/completions/create">
     *     Create completion</a>
     */
    protected Object completion(Object input, String operation, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input = Tool.ME.toDict(input), operation, clazz);
        return doHttp(HttpData.of(Tool.ME).setConfig(config)
                .setMethod(POST).setHttpType(FOUR)
                .setUrl("https://api.openai.com/v1/completions")
                .setHeaders(Dict.of(AUTHORIZATION_KEY, BEARER_KEY + config.getApiKey()))
                .setData((Dict) input));
    }

    /**
     * Creates an image given a prompt.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/images/create">
     *     Create image</a>
     */
    protected Object imageCreate(Object input, String operation, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input = Tool.ME.toDict(input), operation, clazz);
        return doHttp(HttpData.of(Tool.ME).setConfig(config)
                .setMethod(POST).setHttpType(FOUR)
                .setUrl("https://api.openai.com/v1/images/generations")
                .setHeaders(Dict.of(AUTHORIZATION_KEY, BEARER_KEY + config.getApiKey()))
                .setData((Dict) input));
    }

    /**
     * Creates an edited or extended image given an original image and a prompt.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/images/createEdit">
     *     Create image edit</a>
     */
    protected Object imageEdit(Object input, String operation, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input = Tool.ME.toDict(input), operation, clazz);
        return doHttp(HttpData.of(Tool.ME).setConfig(config)
                .setMethod(POST).setHttpType(THREE)
                .setUrl("https://api.openai.com/v1/images/edits")
                .setHeaders(Dict.of(AUTHORIZATION_KEY, BEARER_KEY + config.getApiKey()))
                .setData((Dict) input));
    }

    /**
     * Creates a variation of a given image.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/images/createVariation">
     *     Create image variation</a>
     */
    protected Object imageVariation(Object input, String operation, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input = Tool.ME.toDict(input), operation, clazz);
        return doHttp(HttpData.of(Tool.ME).setConfig(config)
                .setMethod(POST).setHttpType(THREE)
                .setUrl("https://api.openai.com/v1/images/variations")
                .setHeaders(Dict.of(AUTHORIZATION_KEY, BEARER_KEY + config.getApiKey()))
                .setData((Dict) input));
    }

    /**
     * Lists the currently available models, and provides basic information about
     *      each one such as the owner and availability.
     * @param input The required arguments
     * @return The result of operation
     * @see <a href="https://platform.openai.com/docs/api-reference/models/list">
     *     List models</a>
     */
    protected Object modelList(Object input, String operation, Class<?> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Config config = getConfig(input = Tool.ME.toDict(input), operation, clazz);
        return doHttp(HttpData.of(Tool.ME).setConfig(config)
                .setMethod(GET).setHttpType(ONE)
                .setUrl("https://api.openai.com/v1/models")
                .setHeaders(Dict.of(AUTHORIZATION_KEY, BEARER_KEY + config.getApiKey()))
                .setData((Dict) input));
    }

    /**
     * The OpenAI AI handler internal tool.
     * @author Kahle
     */
    protected static class Tool extends InnerTool {
        /**
         * The internal tool instance.
         */
        public static final Tool ME = new Tool();
    }

    /**
     * The OpenAI AI handler configuration.
     * @author Kahle
     */
    public static class Config extends AbstractConfig {
        private String apiKey;

        public Config(String apiKey) {
            Assert.notBlank(apiKey, "Parameter \"apiKey\" must not blank. ");
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
