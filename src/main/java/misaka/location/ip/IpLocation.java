package misaka.location.ip;

import artoria.util.StringUtils;
import baibao.extension.location.Geolocation;

import static artoria.common.Constants.BLANK_SPACE;
import static artoria.common.Constants.EMPTY_STRING;

/**
 * The network physical address object.
 * @see <a href="https://en.wikipedia.org/wiki/IP_address">IP address</a>
 * @author Kahle
 */
@Deprecated // TODO: 2023/3/27 Deletable
public class IpLocation extends Geolocation {
    private String ipAddress;
    private String isp;

    public String getIpAddress() {

        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {

        this.ipAddress = ipAddress;
    }

    public String getIsp() {

        return isp;
    }

    public void setIsp(String isp) {

        this.isp = isp;
    }

    @Override
    public String toString() {
        String country = getCountry();
        String region = getRegion();
        String city = getCity();
        String district = getDistrict();
        String street = getStreet();
        return (StringUtils.isNotBlank(country) ? country + BLANK_SPACE : EMPTY_STRING)
                + (StringUtils.isNotBlank(region) ? region + BLANK_SPACE : EMPTY_STRING)
                + (StringUtils.isNotBlank(city) ? city + BLANK_SPACE : EMPTY_STRING)
                + (StringUtils.isNotBlank(district) ? district + BLANK_SPACE : EMPTY_STRING)
                + (StringUtils.isNotBlank(street) ? street + BLANK_SPACE : EMPTY_STRING)
                + (StringUtils.isNotBlank(isp) ? isp : EMPTY_STRING);
    }

}
