package baibao.ai.nlp.splitter.support;

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
