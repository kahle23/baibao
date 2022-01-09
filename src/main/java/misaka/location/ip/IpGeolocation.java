package misaka.location.ip;

import artoria.util.StringUtils;
import misaka.location.Geolocation;

import static artoria.common.Constants.BLANK_SPACE;
import static artoria.common.Constants.EMPTY_STRING;

/**
 * Network physical address object.
 * @see <a href="https://en.wikipedia.org/wiki/IP_address">IP address</a>
 * @author Kahle
 */
public class IpGeolocation extends Geolocation {
    private String ipAddress;
    private String isp;
    private String org;
    private String timezone;
    private String zip;
    /**
     * Autonomous system number.
     */
    private String as;

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

    public String getOrg() {

        return org;
    }

    public void setOrg(String org) {

        this.org = org;
    }

    public String getTimezone() {

        return timezone;
    }

    public void setTimezone(String timezone) {

        this.timezone = timezone;
    }

    public String getZip() {

        return zip;
    }

    public void setZip(String zip) {

        this.zip = zip;
    }

    public String getAs() {

        return as;
    }

    public void setAs(String as) {

        this.as = as;
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
