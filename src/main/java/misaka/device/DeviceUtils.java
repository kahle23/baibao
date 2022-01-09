package misaka.device;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

public class DeviceUtils {
    private static Logger log = LoggerFactory.getLogger(DeviceUtils.class);
    private static DeviceProvider deviceProvider;

    public static DeviceProvider getDeviceProvider() {

        return deviceProvider;
    }

    public static void setDeviceProvider(DeviceProvider deviceProvider) {
        Assert.notNull(deviceProvider, "Parameter \"deviceProvider\" must not null. ");
        log.info("Set device provider: {}", deviceProvider.getClass().getName());
        DeviceUtils.deviceProvider = deviceProvider;
    }

    public static Device info(String model, String type) {

        return getDeviceProvider().info(model, type);
    }

}
