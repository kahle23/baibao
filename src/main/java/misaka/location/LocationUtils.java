package misaka.location;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import misaka.location.ip.IpGeolocation;
import misaka.location.ip.IpGeolocationProvider;

import java.util.List;

public class LocationUtils {
    private static Logger log = LoggerFactory.getLogger(LocationUtils.class);
    private static IpGeolocationProvider ipGeolocationProvider;
    private static LocationProvider locationProvider;

    public static LocationProvider getLocationProvider() {

        return locationProvider;
    }

    public static void setLocationProvider(LocationProvider locationProvider) {
        Assert.notNull(locationProvider, "Parameter \"locationProvider\" must not null. ");
        log.info("Set location provider: {}", locationProvider.getClass().getName());
        LocationUtils.locationProvider = locationProvider;
    }

    public static IpGeolocationProvider getIpGeolocationProvider() {

        return ipGeolocationProvider;
    }

    public static void setIpGeolocationProvider(IpGeolocationProvider ipGeolocationProvider) {
        Assert.notNull(ipGeolocationProvider, "Parameter \"ipGeolocationProvider\" must not null. ");
        log.info("Set ip geolocation provider: {}", ipGeolocationProvider.getClass().getName());
        LocationUtils.ipGeolocationProvider = ipGeolocationProvider;
    }

    public static Location info(String nameOrCode) {

        return getLocationProvider().info(nameOrCode);
    }

    public static List<Location> search(String nameOrCode) {

        return getLocationProvider().search(nameOrCode);
    }

    public static List<Location> subLocations(String nameOrCode, String paramType) {

        return getLocationProvider().subLocations(nameOrCode, paramType);
    }

    public static IpGeolocation ipGeolocation(String ipAddress) {

        return getIpGeolocationProvider().info(ipAddress);
    }

}
