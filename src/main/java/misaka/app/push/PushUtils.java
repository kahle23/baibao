package misaka.app.push;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PushUtils {
    private static final Map<String, PushProvider> PROVIDER_MAP = new ConcurrentHashMap<String, PushProvider>();
    private static Logger log = LoggerFactory.getLogger(PushUtils.class);

    public static PushProvider unregister(String appId) {
        Assert.notBlank(appId, "Parameter \"appId\" must not blank. ");
        PushProvider remove = PROVIDER_MAP.remove(appId);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Unregister \"{}\" to \"{}\". ", className, appId);
        }
        return remove;
    }

    public static void register(String appId, PushProvider pushProvider) {
        Assert.notBlank(appId, "Parameter \"appId\" must not blank. ");
        Assert.notNull(pushProvider, "Parameter \"pushProvider\" must not null. ");
        String className = pushProvider.getClass().getName();
        log.info("Register \"{}\" to \"{}\". ", className, appId);
        PROVIDER_MAP.put(appId, pushProvider);
    }

    public static PushResult send(PushMessage pushMessage) {
        Assert.notNull(pushMessage, "Parameter \"pushMessage\" must not null. ");
        String appId = pushMessage.getAppId();
        PushProvider pushProvider = PROVIDER_MAP.get(appId);
        if (pushProvider == null) {
            throw new IllegalArgumentException(
                    "No configuration for \"" + appId + "\" was found. ");
        }
        return pushProvider.send(pushMessage);
    }

}
