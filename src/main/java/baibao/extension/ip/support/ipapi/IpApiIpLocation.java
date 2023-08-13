package baibao.extension.ip.support.ipapi;

import artoria.util.StringUtils;
import baibao.extension.ip.IpLocation;

import java.io.Serializable;

import static artoria.common.Constants.BLANK_SPACE;
import static artoria.common.Constants.EMPTY_STRING;

/**
 * Network physical address object.
 * @see <a href="https://en.wikipedia.org/wiki/IP_address">IP address</a>
 * @author Kahle
 */
public class IpApiIpLocation extends IpLocation implements Serializable {
    private String org;
    private String timezone;
    private String zip;
    /**
     * Autonomous system number.
     */
    private String as;

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
                + (StringUtils.isNotBlank(getIsp()) ? getIsp() : EMPTY_STRING);
    }

}
