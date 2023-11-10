package baibao.db.vector.support;

import java.net.Proxy;

public class PineconeVectorDbHandlerImpl extends BasePineconeVectorDbHandler {
    private final Config config;

    public PineconeVectorDbHandlerImpl(String host, String apiKey, Proxy proxy) {

        this.config = new Config(host, apiKey, proxy);
    }

    public PineconeVectorDbHandlerImpl(String host, String apiKey) {

        this(host, apiKey, null);
    }

    @Override
    protected Config getConfig(Object argument) {

        return config;
    }

}
