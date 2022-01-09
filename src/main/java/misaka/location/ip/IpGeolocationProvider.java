package misaka.location.ip;

/**
 * Network physical address provider.
 * @author Kahle
 */
public interface IpGeolocationProvider {

    IpGeolocation info(String ipAddress);

}
