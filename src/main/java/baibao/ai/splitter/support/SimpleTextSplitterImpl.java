/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.ai.splitter.support;

public class SimpleTextSplitterImpl extends BaseSimpleTextSplitter {
    private final Config config;

    public SimpleTextSplitterImpl(Integer chunkSize) {

        this.config = new Config(chunkSize);
    }

    @Override
    protected Config getConfig(Object input) {

        return config;
    }

}
