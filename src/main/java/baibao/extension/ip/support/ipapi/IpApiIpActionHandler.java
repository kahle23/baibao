package baibao.extension.ip.support.ipapi;

import artoria.action.handler.AbstractClassicActionHandler;
import artoria.data.bean.BeanUtils;
import artoria.data.json.JsonUtils;
import artoria.net.http.HttpMethod;
import artoria.net.http.HttpUtils;
import artoria.net.http.support.SimpleRequest;
import artoria.util.MapUtils;
import artoria.util.StringUtils;
import artoria.util.TypeUtils;
import baibao.extension.ip.IpLocation;
import baibao.extension.ip.IpQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Network physical address provider by website(http://ip-api.com).
 * @see <a href="http://ip-api.com/">IP Geolocation API</a>
 * @author Kahle
 */
public class IpApiIpActionHandler extends AbstractClassicActionHandler {
    private static final Logger log = LoggerFactory.getLogger(IpApiIpActionHandler.class);
    private final Class<?>[] supportClasses = new Class[] { IpApiIpLocation.class, IpLocation.class};

    @Override
    public <T> T execute(Object input, Class<T> clazz) {
        isSupport(supportClasses, clazz);
        IpQuery ipQuery = (IpQuery) input;
        String ipAddress = ipQuery.getIpAddress();
        String language = ipQuery.getLanguage();
        if (StringUtils.isBlank(language)) { language = "zh-CN"; }
        SimpleRequest request = new SimpleRequest(
                "http://ip-api.com/json/" + ipAddress + "?lang=" + language, HttpMethod.GET);
        String jsonString = HttpUtils.execute(request).getBodyAsString();
        if (StringUtils.isBlank(jsonString)) { return null; }
        ParameterizedType parameterizedType = TypeUtils.parameterizedOf(Map.class, String.class, String.class);
        Map<String, String> map = JsonUtils.parseObject(jsonString, parameterizedType);
        if (MapUtils.isEmpty(map)) { return null; }
        IpApiIpLocation ipApiIpLocation = new IpApiIpLocation();
        ipApiIpLocation.setIpAddress(ipAddress);
        ipApiIpLocation.setCountry(map.get("country"));
        ipApiIpLocation.setCountryCode(map.get("countryCode"));
        ipApiIpLocation.setRegion(map.get("regionName"));
        ipApiIpLocation.setRegionCode(map.get("region"));
        ipApiIpLocation.setCity(map.get("city"));
        ipApiIpLocation.setCityCode(null);
        ipApiIpLocation.setIsp(map.get("isp"));
        ipApiIpLocation.setOrg(map.get("org"));
        ipApiIpLocation.setTimezone(map.get("timezone"));
        ipApiIpLocation.setZip(map.get("zip"));
        ipApiIpLocation.setAs(map.get("as"));
        String lat = map.get("lat");
        String lon = map.get("lon");
        try {
            BigDecimal latitude = lat != null ? new BigDecimal(lat) : null;
            BigDecimal longitude = lon != null ? new BigDecimal(lon) : null;
            ipApiIpLocation.setLatitude(latitude);
            ipApiIpLocation.setLongitude(longitude);
        }
        catch (Exception e) {
            log.info("Parse latitude and longitude to double error", e);
        }
        return BeanUtils.beanToBean(ipApiIpLocation, clazz);
    }

}
